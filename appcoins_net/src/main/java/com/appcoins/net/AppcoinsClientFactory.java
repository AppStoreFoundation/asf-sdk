package com.appcoins.net;

public class AppcoinsClientFactory {

  public static AppcoinsClient build(String serviceUrl, String packageName, int versionCode,
      Interceptor interceptor) {
    AppcoinsClient appcoinsClient =
        new AppcoinsClient(packageName, versionCode, serviceUrl, interceptor);
    return appcoinsClient;
  }
}
