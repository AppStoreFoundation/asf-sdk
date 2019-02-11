package com.appcoins.net;

public class AppcoinsClientFactory {

  public static AppcoinsClient build(String serviceUrl, String packageName, int versionCode){
    AppcoinsClient appcoinsClient = new AppcoinsClient(packageName,versionCode,serviceUrl);
    return appcoinsClient;
  }
}
