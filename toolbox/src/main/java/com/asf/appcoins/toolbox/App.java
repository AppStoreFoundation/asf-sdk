package com.asf.appcoins.toolbox;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import com.asf.appcoins.sdk.ads.AppCoinsAds;

public class App extends Application {

  private static AppCoinsAds adsSdk;

  @Override public void onCreate() {
    super.onCreate();

    AdvertisementSdkSingleton.create(this, true);

    try {

      adsSdk = AdvertisementSdkSingleton.getAdsSdk();
      adsSdk.init(this);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
  }
}
