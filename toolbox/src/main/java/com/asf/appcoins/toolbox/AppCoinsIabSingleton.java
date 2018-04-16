package com.asf.appcoins.toolbox;

import com.asf.appcoins.sdk.iab.AppCoinsIab;
import com.asf.appcoins.sdk.iab.AppCoinsIabBuilder;
import com.asf.appcoins.sdk.iab.entity.SKU;
import java.util.List;

public class AppCoinsIabSingleton {
  private static AppCoinsIab appCoinsIab;

  private AppCoinsIabSingleton() {
  }

  public static void create(String developerAddress, List<SKU> skusList) {
    appCoinsIab = new AppCoinsIabBuilder(developerAddress).withSkus(skusList)
        .withDebug(false)
        .createAppCoinsIab();
  }

  public static void create(String developerAddress, List<SKU> skusList, boolean debug) {
    appCoinsIab = new AppCoinsIabBuilder(developerAddress).withSkus(skusList)
        .withDebug(debug)
        .createAppCoinsIab();
  }

  public static AppCoinsIab getAppCoinsIab() {
    if (appCoinsIab == null) {
      throw new IllegalStateException("Instance is null!");
    }

    return appCoinsIab;
  }
}
