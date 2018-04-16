package com.asf.appcoins.toolbox;

import com.asf.appcoins.sdk.iab.AppCoinsIab;
import com.asf.appcoins.sdk.iab.AppCoinsIabBuilder;
import com.asf.appcoins.sdk.iab.entity.SKU;
import java.util.List;

public class AppCoinsSdkSingleton {
  private static AppCoinsIab appCoinsIab;

  private AppCoinsSdkSingleton() {
  }

  public static void create(String developerAddress, List<SKU> skusList) {
    appCoinsIab = new AppCoinsIabBuilder(developerAddress).withSkus(skusList)
        .withDebug(false)
        .createAppCoinsSdk();
  }

  public static void create(String developerAddress, List<SKU> skusList, boolean debug) {
    appCoinsIab = new AppCoinsIabBuilder(developerAddress).withSkus(skusList)
        .withDebug(debug)
        .createAppCoinsSdk();
  }

  public static AppCoinsIab getAppCoinsIab() {
    if (appCoinsIab == null) {
      throw new IllegalStateException("Instance is null!");
    }

    return appCoinsIab;
  }
}
