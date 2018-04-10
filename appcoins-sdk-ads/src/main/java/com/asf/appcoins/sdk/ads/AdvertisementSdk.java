package com.asf.appcoins.sdk.ads;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */

/** The interface for the Advertisement sdk */
public interface AdvertisementSdk {

  /**
   * Method that starts the handshake that will find the service/wallet that can handle the PoA
   * process.
   */
  void handshake();

  /**
   * Method that send a proof the wallet that is listening to our PoA process.
   */
  void sendProof();

  /**
   * Method to register the campaign for the proof of attention.
   * @param campaignId The campaign id to be added to the message bundle.
   */
  void registerCampaign(String campaignId);

  /**
   * Method to initialize the Advertisement SDK.
   * @param application The application instance of the app.
   */
  void init(Application application);

}
