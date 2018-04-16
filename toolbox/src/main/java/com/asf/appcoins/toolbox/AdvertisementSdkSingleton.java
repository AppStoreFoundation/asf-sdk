package com.asf.appcoins.toolbox;

import android.content.Context;
import com.asf.appcoins.sdk.ads.AppCoinsAds;
import com.asf.appcoins.sdk.ads.AppCoinsAdsBuilder;

public class AdvertisementSdkSingleton {
  private static AppCoinsAds adsSdk;

  private AdvertisementSdkSingleton() {
  }

  public static void create(Context context, boolean debug) {
    adsSdk = new AppCoinsAdsBuilder().withDebug(debug)
        .createAdvertisementSdk(context);
  }

  public static void create(Context context) {
    adsSdk = new AppCoinsAdsBuilder().createAdvertisementSdk(context);
  }

  public static AppCoinsAds getAdsSdk() {
    if (adsSdk == null) {
      throw new IllegalStateException("Instance is null!");
    }

    return adsSdk;
  }
}
