package com.asf.appcoins.sdk.ads.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

/**
 * Class that provides lifecycle for the whole application process.
 * <p>
 * You can consider this LifecycleOwner as the composite of all of your Activities, except that
 * {@link Lifecycle.Event#ON_CREATE} will be dispatched once and {@link Lifecycle.Event#ON_DESTROY}
 * will never be dispatched. Other lifecycle events will be dispatched with following rules:
 * ProcessLifecycleOwner will dispatch {@link Lifecycle.Event#ON_START},
 * {@link Lifecycle.Event#ON_RESUME} events, as a first activity moves through these events.
 * {@link Lifecycle.Event#ON_PAUSE}, {@link Lifecycle.Event#ON_STOP}, events will be dispatched with
 * a <b>delay</b> after a last activity
 * passed through them. This delay is long enough to guarantee that ProcessLifecycleOwner
 * won't send any events if activities are destroyed and recreated due to a
 * configuration change.
 *
 * <p>
 * It is useful for use cases where you would like to react on your app coming to the foreground or
 * going to the background and you don't need a milliseconds accuracy in receiving lifecycle
 * events.
 */
@SuppressWarnings("WeakerAccess") public class ProcessLifecycleOwner implements LifecycleOwner {

  static final long TIMEOUT_MS = 700; //mls
  private static final ProcessLifecycleOwner sInstance = new ProcessLifecycleOwner();
  private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
  // ground truth counters
  private int mStartedCounter = 0;
  private int mResumedCounter = 0;
  private boolean mPauseSent = true;
  private boolean mStopSent = true;
  private Handler mHandler;
  private Runnable mDelayedPauseRunnable = new Runnable() {
    @Override public void run() {
      dispatchPauseIfNeeded();
      dispatchStopIfNeeded();
    }
  };
  ReportFragment.ActivityInitializationListener mInitializationListener =
      new ReportFragment.ActivityInitializationListener() {
        @Override public void onCreate() {
        }

        @Override public void onStart() {
          activityStarted();
        }

        @Override public void onResume() {
          activityResumed();
        }
      };

  private ProcessLifecycleOwner() {
  }

  /**
   * The LifecycleOwner for the whole application process. Note that if your application
   * has multiple processes, this provider does not know about other processes.
   *
   * @return {@link LifecycleOwner} for the whole application.
   */
  public static LifecycleOwner get() {
    return sInstance;
  }

  public static void init(Context context) {
    sInstance.attach(context);
  }

  void activityStarted() {
    mStartedCounter++;
    if (mStartedCounter == 1 && mStopSent) {
      mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
      mStopSent = false;
    }
  }

  void activityResumed() {
    mResumedCounter++;
    if (mResumedCounter == 1) {
      if (mPauseSent) {
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        mPauseSent = false;
      } else {
        mHandler.removeCallbacks(mDelayedPauseRunnable);
      }
    }
  }

  void activityPaused() {
    mResumedCounter--;
    if (mResumedCounter == 0) {
      mHandler.postDelayed(mDelayedPauseRunnable, TIMEOUT_MS);
    }
  }

  void activityStopped() {
    mStartedCounter--;
    dispatchStopIfNeeded();
  }

  void dispatchPauseIfNeeded() {
    if (mResumedCounter == 0) {
      mPauseSent = true;
      mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }
  }

  void dispatchStopIfNeeded() {
    if (mStartedCounter == 0 && mPauseSent) {
      mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
      mStopSent = true;
    }
  }

  void attach(Context context) {
    mHandler = new Handler();
    mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    Application app = (Application) context.getApplicationContext();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      app.registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
        @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
          ReportFragment.get(activity)
              .setProcessListener(mInitializationListener);
        }

        @Override public void onActivityPaused(Activity activity) {
          activityPaused();
        }

        @Override public void onActivityStopped(Activity activity) {
          activityStopped();
        }
      });
    }
  }

  @Override public Lifecycle getLifecycle() {
    return mRegistry;
  }
}
