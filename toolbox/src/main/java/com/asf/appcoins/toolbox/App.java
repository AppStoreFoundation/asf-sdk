package com.asf.appcoins.toolbox;

import android.app.Application;
import android.content.pm.PackageManager;
import com.appcoins.sdk.billing.helpers.WalletUtils;

public class App extends Application {

  @Override public void onCreate() {
    super.onCreate();

    WalletUtils.initIap(this);
    AdvertisementSdkSingleton.create(this, true);
    try {
      AdvertisementSdkSingleton.getAdsSdk()
          .init(this);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
  }
}
