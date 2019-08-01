package com.asf.appcoins.sdk.ads.poa;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Joao Raimundo on 28/03/2018.
 */

public interface PoAServiceConnector {

  /**
   * Information for the shared preferences used by the Proof of Attention mechanism.
   */
  /** The shared preferences name */
  String SHARED_PREFS = "sdk_prefs";
  /** The preference name for the package name of the wallet that answered to our handshake */
  String PREFERENCE_WALLET_PCKG_NAME = "WALLET_PCKG_NAME";
  /**
   * Actions used to communicate with the service on the wallet side, for the handshake start.
   */
  String ACTION_START_HANDSHAKE = "com.asf.appcoins.service.ACTION_START_HANDSHAKE";
  /**
   * Actions used to communicate with the service on the wallet side, for the bind.
   */
  String ACTION_BIND = "com.asf.appcoins.service.ACTION_BIND";

  /**
   * Intent parameter for the wallet package name, that was obtained on the handshake.
   */
  String PARAM_WALLET_PACKAGE_NAME = "PARAM_WALLET_PKG_NAME";
  /**
   * Intent parameter for the application package name, to be used ont the second step of the
   * handshake.
   */
  String PARAM_APP_PACKAGE_NAME = "PARAM_APP_PKG_NAME";
  /**
   * Intent parameter for the application service name, to be used ont the second step of the
   * handshake.
   */
  String PARAM_APP_SERVICE_NAME = "PARAM_APP_SERVICE_NAME";
  /**
   * Intent parameter for the application service name, to identify the network we are using.
   */
  String PARAM_NETWORK_ID = "PARAM_NETWORK_ID";

  /**
   * Method that starts the handshake process, that obtains the wallet that is listening to our
   * broadcast and that manages the PoA process.
   *
   * @param context The context where this connector is being used.
   * @param networkId The id of the network in use.
   */
  void startHandshake(Context context, int networkId);

  /**
   * Method to bind to the service on the received package name and listening to the for the given
   * action of the intent filter.
   *
   * @param context The context where this connector is being used.
   *
   * @retun true if the bind was successful, false otherwise.
   */
  boolean connectToService(Context context);

  /**
   * Method to unbind with the service that handles the PoA process.
   *
   * @param context The context where this connector is being used.
   */
  void disconnectFromService(Context context);

  /**
   * Method to send the message to the bound service.
   *
   * @param context The context where this connector is being used.
   * @param messageType The type of message being send. Possible values at the moment:
   * {MSG_REGISTER_CAMPAIGN, MSG_SEND_PROOF and MSG_SIGN_PROOF}.
   * @param content The package name where the service is located.
   */
  void sendMessage(Context context, int messageType, Bundle content);
}
