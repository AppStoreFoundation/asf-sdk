package com.asf.appcoins.toolbox;

import com.asf.appcoins.sdk.iab.AppCoinsSdk;
import com.asf.appcoins.sdk.iab.AppCoinsSdkBuilder;
import com.asf.appcoins.sdk.iab.entity.SKU;
import java.util.List;

public class AppCoinsSdkSingleton {
  private static AppCoinsSdk appCoinsSdk;

  private AppCoinsSdkSingleton() {
  }

  public static void create(String developerAddress, List<SKU> skusList) {
    appCoinsSdk = new AppCoinsSdkBuilder(developerAddress).withSkus(skusList)
        .withDebug(true)
        .createAppCoinsSdk();
  }

  public static AppCoinsSdk getAppCoinsSdk() {
    if (appCoinsSdk == null) {
      throw new IllegalStateException("Instance is null!");
    }

    return appCoinsSdk;
  }
}
