package com.appcoins.net;

public class AppcoinsClientFactory {

  public static AppcoinsClient build(String serviceUrl) {
    AppcoinsHTTPClient appcoinsHTTPCLient = new AppcoinsHTTPClient(serviceUrl);
    appcoinsHTTPCLient.startService();
    AppcoinsClient appcoinsClient = new AppcoinsClient(appcoinsHTTPCLient);
    return appcoinsClient;
  }
}
