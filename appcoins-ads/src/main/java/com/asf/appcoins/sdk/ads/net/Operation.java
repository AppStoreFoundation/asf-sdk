package com.asf.appcoins.sdk.ads.net;

public abstract class Operation {

  public abstract String mapParams(String packageName, String versionCode, QueryParams queryParams);

  public abstract AppCoinsClientResponse mapResponse(String response);
}
