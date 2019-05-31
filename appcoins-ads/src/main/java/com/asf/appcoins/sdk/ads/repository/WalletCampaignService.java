package com.asf.appcoins.sdk.ads.repository;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.appcoins.advertising.AppCoinsAdvertising;

public class WalletCampaignService implements AppCoinsAdvertising {

  private AppCoinsAdvertising service;

  public WalletCampaignService(IBinder service){
    this.service = AppCoinsAdvertising.Stub.asInterface(service);
  }

  @Override public Bundle getAvailableCampaign() throws RemoteException {
    return service.getAvailableCampaign();
  }

  @Override public IBinder asBinder() {
    return null;
  }
}
