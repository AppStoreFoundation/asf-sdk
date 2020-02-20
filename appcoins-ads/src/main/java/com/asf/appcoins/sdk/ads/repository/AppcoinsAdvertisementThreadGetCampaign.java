package com.asf.appcoins.sdk.ads.repository;

import android.os.Bundle;
import android.os.RemoteException;
import com.asf.appcoins.sdk.ads.network.listeners.GetCampaignResponseListener;

public class
AppcoinsAdvertisementThreadGetCampaign implements Runnable {

  private GetCampaignResponseListener getCampaignResponseListener;
  private AppcoinsAdvertisementRepository appcoinsAdvertisementRepository;

  public AppcoinsAdvertisementThreadGetCampaign(
      GetCampaignResponseListener getCampaignResponseListener,
      AppcoinsAdvertisementRepository appcoinsAdvertisementRepository) {
    this.getCampaignResponseListener = getCampaignResponseListener;
    this.appcoinsAdvertisementRepository = appcoinsAdvertisementRepository;
  }

  @Override public void run() {
    try {
      Bundle response = appcoinsAdvertisementRepository.getAvailableCampaign();
      if(response != null){
        getCampaignResponseListener.responseGetCampaignWallet(response);
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
}
