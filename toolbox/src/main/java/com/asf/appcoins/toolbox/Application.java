package com.asf.appcoins.toolbox;

import android.content.pm.PackageManager;
import com.asf.appcoins.sdk.ads.AppCoinsAds;
import com.asf.appcoins.sdk.ads.AppCoinsAdsBuilder;

public class Application extends android.app.Application {

  public final static String developerAddress = "0x75e8BdCf84B9bd585753dB64A8637A52Bd46f851";

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

  public String getDeveloperAddress() {
    return developerAddress;
  }
}
