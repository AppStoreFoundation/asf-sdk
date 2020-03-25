package com.asf.appcoins.sdk.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Class tha handle swith the activity life cycle so it can be used on the PoA process.
 *
 * Created by Joao Raimundo on 09/04/2018.
 */

@SuppressLint("NewApi") //No requiresApi annotation
public class LifeCycleListener implements Application.ActivityLifecycleCallbacks {

  private static final String TAG = LifeCycleListener.class.getSimpleName();
  private static LifeCycleListener instance;
  private int started = -1;
  private int resumed = -1;
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

  private boolean isAppStarted() {
    return started != -1;
  }

  private boolean isAppResumed() {
    return resumed != -1;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
  }

  @Override public void onActivityStarted(Activity activity) {
    if (!isAppStarted() && listener != null) {
      listener.onBecameForeground(activity);
    }
    started = activity.hashCode();
  }

  @Override public void onActivityResumed(Activity activity) {
    if (!isAppStarted() && !isAppResumed() && listener != null) {
      listener.onBecameForeground(activity);
    }
    resumed = activity.hashCode();
  }

  @Override public void onActivityPaused(Activity activity) {
    if (resumed == activity.hashCode()) {
      resumed = -1;
    }
  }

  @Override public void onActivityStopped(Activity activity) {
    if (started == activity.hashCode()) {
      started = -1;
      if (listener != null) {
        listener.onBecameBackground();
      }
    }
  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
  }

  @Override public void onActivityDestroyed(Activity activity) {

  }

  public interface Listener {
    void onBecameForeground(Activity activity);

    void onBecameBackground();
  }
}
