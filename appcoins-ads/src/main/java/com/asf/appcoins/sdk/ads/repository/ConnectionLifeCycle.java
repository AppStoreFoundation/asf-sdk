package com.asf.appcoins.sdk.ads.repository;

import android.os.IBinder;

public interface ConnectionLifeCycle {
  void onConnect(IBinder service, final AppcoinsAdvertisementListenner listener);

  void onDisconnect(final AppcoinsAdvertisementListenner listener);
}
