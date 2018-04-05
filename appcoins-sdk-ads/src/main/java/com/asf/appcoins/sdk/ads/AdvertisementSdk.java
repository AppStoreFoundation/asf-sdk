package com.asf.appcoins.sdk.ads;

import android.content.Context;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */

/** The interface for the Advertisement sdk */
public interface AdvertisementSdk {

  /**
   * Method that starts the handshake that will find the service/wallet that can handle the PoA
   * process.
   * @param context The context of the instance calling the sdk.
   */
  void handshake(Context context);

  /**
   * Method that send a proof the wallet that is listening to our PoA process.
   * @param context The context of the instance calling the sdk.
   */
  void sendProof(Context context);

  /**
   * Method that send the campaign id used for the PoA process.
   * @param context The context of the instance calling the sdk.
   */
  void registerCampaign(Context context);

}
