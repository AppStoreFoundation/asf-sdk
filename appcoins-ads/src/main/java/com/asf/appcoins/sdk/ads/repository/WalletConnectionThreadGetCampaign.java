package com.asf.appcoins.sdk.ads.repository;

import android.os.Bundle;
import android.os.RemoteException;
import com.asf.appcoins.sdk.ads.network.listeners.GetCampaignResponseListener;

public class WalletConnectionThreadGetCampaign implements Runnable {

  private GetCampaignResponseListener getCampaignResponseListener;
  private WalletCampaignRepository walletCampaignRepository;

  public WalletConnectionThreadGetCampaign(GetCampaignResponseListener getCampaignResponseListener,
      WalletCampaignRepository walletCampaignRepository) {
    this.getCampaignResponseListener = getCampaignResponseListener;
    this.walletCampaignRepository = walletCampaignRepository;
  }

  @Override public void run() {
    try {
      Bundle response = walletCampaignRepository.getAvailableCampaign();
      getCampaignResponseListener.responseGetCampaignWallet(response);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
}
