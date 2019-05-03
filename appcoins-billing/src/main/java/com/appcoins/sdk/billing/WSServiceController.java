package com.appcoins.sdk.billing;

import java.util.List;

public class WSServiceController {

  private static String wsUrl;

  public static void setWsUrl(String url) {
    wsUrl = url;
  }

  public static String GetSkuDetailsService(String packageName, List<String> sku) {
    GetSkuDetailsService getSkuDetailsService = new GetSkuDetailsService(wsUrl, packageName, sku);
    return getSkuDetailsService.getSkuDetailsForPackageName();
  }
}
