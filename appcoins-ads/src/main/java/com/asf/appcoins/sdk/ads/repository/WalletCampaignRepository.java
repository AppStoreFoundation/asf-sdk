package com.asf.appcoins.sdk.ads.repository;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

public class WalletCampaignRepository implements ConnectionLifeCycle {

  private WalletCampaignService service;

  @Override public void onConnect(IBinder service, AppcoinsAdvertisementListenner listener) {
    this.service = new WalletCampaignService(service);
    listener.onAdvertisementFinished(ResponseCode.OK.getValue());
  }

  @Override public void onDisconnect(AppcoinsAdvertisementListenner listener) {

  }

  public Bundle getAvailableCampaign() throws RemoteException {
    return service.getAvailableCampaign();
  }
}
