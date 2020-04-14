package com.appcoins.sdk.billing.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.appcoins.communication.requester.ActivityProvider;

public class LifecycleActivityProvider
    implements Application.ActivityLifecycleCallbacks, ActivityProvider {
  private Activity activity;

  @SuppressLint("ObsoleteSdkInt") public LifecycleActivityProvider(Context context) {

    Context applicationContext = context.getApplicationContext();
    if (applicationContext instanceof Application
        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      ((Application) applicationContext).registerActivityLifecycleCallbacks(this);
    }
  }

  @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    this.activity = activity;
  }

  @Override public void onActivityStarted(Activity activity) {
    this.activity = activity;
  }

  @Override public void onActivityResumed(Activity activity) {
    this.activity = activity;
  }

  @Override public void onActivityPaused(Activity activity) {

  }

  @Override public void onActivityStopped(Activity activity) {

  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

  }

  @Override public void onActivityDestroyed(Activity activity) {
    if (activity == this.activity) {
      this.activity = null;
    }
  }

  @Override public Activity getActivity() {
    if (activity == null) {
      Log.w(LifecycleActivityProvider.class.getSimpleName(), "getInstance: ",
          new IllegalStateException("activity reference is null"));
    }
    return activity;
  }
}
