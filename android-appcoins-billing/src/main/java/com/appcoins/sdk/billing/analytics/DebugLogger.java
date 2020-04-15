package com.appcoins.sdk.billing.analytics;

import android.util.Log;
import cm.aptoide.analytics.AnalyticsLogger;
import com.appcoins.billing.sdk.BuildConfig;

class DebugLogger implements AnalyticsLogger {
  @Override public void logDebug(String tag, String msg) {
    if (BuildConfig.DEBUG) {
      Log.d(tag, msg);
    }
  }

  @Override public void logWarningDebug(String TAG, String msg) {
    if (BuildConfig.DEBUG) {
      Log.d(TAG, msg);
    }
  }
}
