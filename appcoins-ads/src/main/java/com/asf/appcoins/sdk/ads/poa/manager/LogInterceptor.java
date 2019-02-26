package com.asf.appcoins.sdk.ads.poa.manager;

import android.util.Log;
import com.asf.appcoins.sdk.ads.net.logs.Interceptor;

public class LogInterceptor implements Interceptor {

  private static final String TAG = "HTTP_TRACE";

  @Override public void OnInterceptPublish(String log) {
    Log.d(TAG, log);
  }
}
