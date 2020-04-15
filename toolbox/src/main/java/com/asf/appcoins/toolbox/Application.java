package com.asf.appcoins.toolbox;

import android.content.pm.PackageManager;
import com.asf.appcoins.sdk.ads.AppCoinsAds;
import com.asf.appcoins.sdk.ads.AppCoinsAdsBuilder;

public class Application extends android.app.Application {

  private static AppCoinsAds adsSdk;

  @Override public void onCreate() {
    super.onCreate();

    adsSdk = new AppCoinsAdsBuilder().withDebug(false)
        .createAdvertisementSdk(this);
    try {
      adsSdk.init(this);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
  }
}
