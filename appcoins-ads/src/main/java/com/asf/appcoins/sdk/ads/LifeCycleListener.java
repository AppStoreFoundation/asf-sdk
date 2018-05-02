package com.asf.appcoins.sdk.ads;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import java.lang.ref.WeakReference;

/**
 * Class tha handle swith the activity life cycle so it can be used on the PoA process.
 *
 * Created by Joao Raimundo on 09/04/2018.
 */

public class LifeCycleListener implements Application.ActivityLifecycleCallbacks {

  /** Delay value used to handle time between screen changes. */
  private static final long CHECK_DELAY = 2000;
  /** Instance of the lifecycle */
  private static LifeCycleListener instance;
  /** boolean used to control the current state of the application */
  private boolean foreground;
  /** Reference of the activity used for foreground/background state */
  private WeakReference<Activity> currentActivity;
  /** Handler for delayed task to check if we are in the foreground or in the background */
  private Handler handler = new Handler();
  /**
   * The runnable task reference, used to remove it from the handler when the is still on the
   * foreground but the life cycle triggered a possible background state before.
   */
  private Runnable check;
  /** The listener for the lifecycle background and foreground state */
  private Listener listener;

  /**
   * Method to initialize the lifecycle listener.
   *
   * @param application The application context.
   */
  public static LifeCycleListener init(Application application) {
    if (instance == null) {
      instance = new LifeCycleListener();
      application.registerActivityLifecycleCallbacks(instance);
    }
    return instance;
  }

  /**
   * Getter for the lifecycle listener.
   *
   * @param application The application context.
   */
  public static LifeCycleListener get(Application application) {
    if (instance == null) {
      init(application);
    }
    return instance;
  }

  /**
   * Method to set the listener to the lifecycle events that detect a background/foreground state.
   */
  public void setListener(Listener listener) {
    this.listener = listener;
  }

  @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
  }

  @Override public void onActivityStarted(Activity activity) {
    currentActivity = new WeakReference<>(activity);
    // remove any scheduled checks since we're starting another activity
    // we're definitely not going background
    if (check != null) {
      handler.removeCallbacks(check);
    }

    // check if we're becoming foreground and notify listeners
    if (!foreground && (activity != null && !activity.isChangingConfigurations())) {
      foreground = true;
      if (listener != null) {
        listener.onBecameForeground(activity);
      }
    }
  }

  @Override public void onActivityResumed(Activity activity) {
  }

  @Override public void onActivityPaused(Activity activity) {
    // if we're changing configurations we aren't going background so
    // no need to schedule the check
    if (!activity.isChangingConfigurations()) {
      // don't prevent activity being gc'd
      final WeakReference<Activity> ref = new WeakReference<>(activity);
      handler.postDelayed(check = () -> onActivityClosed(ref.get()), CHECK_DELAY);
    }
  }

  @Override public void onActivityStopped(Activity activity) {
    if (check != null) {
      handler.removeCallbacks(check);
    }
    onActivityClosed(activity);
  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
  }

  @Override public void onActivityDestroyed(Activity activity) {
  }

  /**
   * Method called when the activity was considered close. This method validates if we are till on
   * foreground in case an activity was closed but a new one was opened.
   *
   * @param activity The  activity that was in foreground when this method was triggered.
   */
  private void onActivityClosed(Activity activity) {
    if (foreground) {
      if ((activity == currentActivity.get()) && (activity != null
          && !activity.isChangingConfigurations())) {
        foreground = false;
        if (listener != null) {
          listener.onBecameBackground();
        }
      }
    }
  }

  public interface Listener {
    void onBecameForeground(Activity activity);

    void onBecameBackground();
  }
}
