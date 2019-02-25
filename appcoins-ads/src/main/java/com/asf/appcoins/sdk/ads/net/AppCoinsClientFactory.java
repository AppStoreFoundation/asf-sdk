package com.asf.appcoins.sdk.ads.net;

public class AppCoinsClientFactory {

  public static AppCoinsClient build(String serviceUrl, String packageName, int versionCode,
      Interceptor interceptor) {
    AppCoinsClient appcoinsClient =
        new AppCoinsClient(packageName, versionCode, serviceUrl, interceptor);
    return appcoinsClient;
  }
}
