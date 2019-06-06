package com.asf.appcoins.sdk.ads.repository;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.appcoins.advertising.AppCoinsAdvertising;

public class AppcoinsAdvertisementRepository implements ConnectionLifeCycle {

  private AppCoinsAdvertising service;

  @Override public void onConnect(IBinder service, AppcoinsAdvertisementListenner listener) {
    this.service = AppCoinsAdvertising.Stub.asInterface(service);
    listener.onAdvertisementFinished(ResponseCode.OK.getValue());
  }

  @Override public void onDisconnect(AppcoinsAdvertisementListenner listener) {

  }

  public Bundle getAvailableCampaign() throws RemoteException {
    return service.getAvailableCampaign();
  }
}
