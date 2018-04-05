package com.asf.appcoins.toolbox;

import android.content.Context;
import com.asf.appcoins.sdk.ads.AdvertisementSdk;
import com.asf.appcoins.sdk.ads.AdvertisementSdkBuilder;

public class AdvertisementSdkSingleton {
  private static AdvertisementSdk adsSdk;

  private AdvertisementSdkSingleton() {
  }

  public static void create(Context context) {
    adsSdk = new AdvertisementSdkBuilder().createAdvertisementSdk(context);
  }

  public static AdvertisementSdk getAdsSdk() {
    if (adsSdk == null) {
      throw new IllegalStateException("Instance is null!");
    }

    return adsSdk;
  }
}
