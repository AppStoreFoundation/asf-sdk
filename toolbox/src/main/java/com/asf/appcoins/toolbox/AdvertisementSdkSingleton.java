package com.asf.appcoins.toolbox;

import com.asf.appcoins.sdk.ads.AdvertisementSdk;
import com.asf.appcoins.sdk.ads.AdvertisementSdkBuilder;
import com.asf.appcoins.sdk.iab.AppCoinsSdk;
import com.asf.appcoins.sdk.iab.AppCoinsSdkBuilder;
import com.asf.appcoins.sdk.iab.entity.SKU;

import java.util.List;

public class AdvertisementSdkSingleton {
  private static AdvertisementSdk adsSdk;

  private AdvertisementSdkSingleton() {
  }

  public static void create() {
    adsSdk = new AdvertisementSdkBuilder().createAdvertisementSdk();
  }

  public static AdvertisementSdk getAdsSdk() {
    if (adsSdk == null) {
      throw new IllegalStateException("Instance is null!");
    }

    return adsSdk;
  }
}
