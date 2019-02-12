package com.asf.appcoins.sdk.ads.poa.manager;

import android.util.Log;
import com.appcoins.net.Interceptor;

public class LogInterceptor implements Interceptor {
  @Override public void OnInterceptPublish(String log) {
    Log.d("messaggeee", log);
  }
}
