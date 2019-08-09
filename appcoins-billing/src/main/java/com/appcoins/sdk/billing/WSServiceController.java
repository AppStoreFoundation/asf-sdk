package com.appcoins.sdk.billing;

import java.util.List;

public class WSServiceController {

  public static String getSkuDetailsService(String url,String packageName, List<String> sku) {
    GetSkuDetailsService getSkuDetailsService = new GetSkuDetailsService(url, packageName, sku);
    return getSkuDetailsService.getSkuDetailsForPackageName();
  }
}
