package com.asf.appcoins.sdk.ads;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnector;
import com.asf.appcoins.sdk.ads.poa.manager.PoAManager;
import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import org.web3j.abi.datatypes.Address;

import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_REGISTER_CAMPAIGN;
import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_SEND_PROOF;
import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_SET_NETWORK;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */

final class AppCoinsAdsImpl implements AppCoinsAds {

  private static final String ADS_PREFERENCES = "AppCoinsAds";

  private final PoAServiceConnector poaConnector;
  Address contractAddress;
  AsfWeb3j web3j;
  private Context context;
  private int networkId;

  AppCoinsAdsImpl(PoAServiceConnector poaConnector, int networkId, AsfWeb3j asfWeb3j,
      Address contractAddress) {
    this.poaConnector = poaConnector;
    this.networkId = networkId;
    this.contractAddress = contractAddress;
    this.web3j = asfWeb3j;
  }

  @Override public void handshake() {
    poaConnector.startHandshake(context, networkId);
  }

  @Override public void sendProof() {
    poaConnector.connectToService(context);
    long timestamp = System.currentTimeMillis();
    Bundle bundle = new Bundle();
    bundle.putString("packageName", context.getPackageName());
    bundle.putLong("timeStamp", timestamp);
    poaConnector.sendMessage(context, MSG_SEND_PROOF, bundle);
  }

  @Override public void registerCampaign(String campaignId) {
    poaConnector.connectToService(context);
    Bundle bundle = new Bundle();
    bundle.putString("packageName", context.getPackageName());
    bundle.putString("campaignId", campaignId);
    poaConnector.sendMessage(context, MSG_REGISTER_CAMPAIGN, bundle);
  }

  @Override public void setNetwork(int networkId) {
    if (poaConnector.connectToService(context)) {
      Bundle bundle = new Bundle();
      bundle.putString("packageName", context.getPackageName());
      bundle.putInt("networkId", networkId);
      poaConnector.sendMessage(context, MSG_SET_NETWORK, bundle);
    }
  }

  @Override public void init(Application application) {
    this.context = application;
    PoAManager.init(application, poaConnector, networkId, web3j, contractAddress);
    LifeCycleListener.get(application)
        .setListener(PoAManager.get());
  }
}
