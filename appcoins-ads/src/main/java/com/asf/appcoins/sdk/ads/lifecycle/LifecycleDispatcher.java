package com.asf.appcoins.sdk.ads.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * When initialized, it hooks into the Activity callback of the Application and observes
 * Activities. It is responsible to hook in child-fragments to activities and fragments to report
 * their lifecycle events. Another responsibility of this class is to mark as stopped all lifecycle
 * providers related to an activity as soon it is not safe to run a fragment transaction in this
 * activity.
 */
public class LifecycleDispatcher {

  private static AtomicBoolean sInitialized = new AtomicBoolean(false);

  private LifecycleDispatcher() {
  }

  public static void init(Context context) {
    if (sInitialized.getAndSet(true)) {
      return;
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(
          new DispatcherActivityCallback());
    }
  }

  static class DispatcherActivityCallback extends EmptyActivityLifecycleCallbacks {

    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
      ReportFragment.injectIfNeededIn(activity);
    }

    @Override public void onActivityStopped(Activity activity) {
    }

    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }
  }
}
