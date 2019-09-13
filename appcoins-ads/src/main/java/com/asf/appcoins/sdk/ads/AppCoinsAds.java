package com.asf.appcoins.sdk.ads;

import android.app.Application;
import android.content.pm.PackageManager;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */

/** The interface for the Advertisement sdk */
public interface AppCoinsAds {

  int NETWORK_MAIN = 1;
  int NETWORK_ROPSTEN = 3;

  /**
   * Method that send a proof the wallet that is listening to our PoA process.
   */
  void sendProof();

  /**
   * Method to register the campaign for the proof of attention.
   *
   * @param campaignId The campaign id to be added to the message bundle.
   */
  void registerCampaign(String campaignId);

  /**
   * Method to register the campaign for the proof of attention.
   *
   * @param networkId The network where the advertisement process is done.
   */
  void setNetwork(int networkId);

  /**
   * Method to initialize the Advertisement SDK.
   *
   * @param application The application instance of the app.
   */
  void init(Application application) throws PackageManager.NameNotFoundException;
}
