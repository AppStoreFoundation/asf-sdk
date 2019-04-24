package com.appcoins.sdk.billing;

import java.util.List;

public class WSServiceController {
  private String url;

  public WSServiceController(final String url) {
    this.url = url;
  }

  public void IsBillingSupportedService(String packageName, String type,
      IsBillingSupportedServiceListenner isBillingSupportedServiceListenner) {
    IsBillingSupportedService isBillingSupportedService =
        new IsBillingSupportedService(url, packageName, type, isBillingSupportedServiceListenner);
    Thread t = new Thread(isBillingSupportedService);
    t.start();
  }

  public String GetSkuDetailsService(String packageName, String type, List<String> sku) {
    GetSkuDetailsService getSkuDetailsService = new GetSkuDetailsService(url, packageName, sku);
    return getSkuDetailsService.getSkuDetailsForPackageName();
  }
}
