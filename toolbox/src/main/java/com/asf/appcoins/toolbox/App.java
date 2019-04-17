package com.asf.appcoins.toolbox;

import android.app.Application;
import android.content.pm.PackageManager;

public class App extends Application {

  @Override public void onCreate() {
    super.onCreate();
/*
    AdvertisementSdkSingleton.create(this, true);
    try {
      AdvertisementSdkSingleton.getAdsSdk()
          .init(this);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }*/
  }
}
