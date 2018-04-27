package com.asf.appcoins.sdk.ads;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.asf.appcoins.sdk.core.util.wallet.WalletUtils;

class InstallWalletLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

  private static final String FIRST_RUN_KEY = "firstRun";

  private boolean firstRun;

  InstallWalletLifecycleCallbacks(SharedPreferences sharedPreferences) {
    this.firstRun = loadFirstRun(sharedPreferences);
  }

  private boolean loadFirstRun(SharedPreferences sharedPreferences) {
    boolean firstRun = sharedPreferences.getBoolean(FIRST_RUN_KEY, true);

    if (firstRun) {
      sharedPreferences.edit()
          .putBoolean(FIRST_RUN_KEY, false)
          .apply();
    }

    return firstRun;
  }

  @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
  }

  @Override public void onActivityStarted(Activity activity) {
  }

  @Override public void onActivityResumed(Activity activity) {
    if (firstRun && !WalletUtils.hasWalletInstalled(activity)) {
      WalletUtils.promptToInstallWallet(activity);
      firstRun = false;
    }
  }

  @Override public void onActivityPaused(Activity activity) {
  }

  @Override public void onActivityStopped(Activity activity) {
  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
  }

  @Override public void onActivityDestroyed(Activity activity) {
  }
}
