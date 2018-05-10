package com.asf.appcoins.sdk.ads.poa;

import android.os.Message;

/**
 * Created by Joao Raimundo on 03/04/2018.
 */

public interface MessageListener {

  /** WARNING: The values for the messages are used on both, sdk and wallet side. So when a new is
   * message value is added on any side please replicate that change of the interface that is
   * missing it.
   */
  /**
   * Command to the service to register the Ad campaign
   */
  int MSG_REGISTER_CAMPAIGN = 1;
  /**
   * Command to the service to send a proof of attention (PoA)
   */
  int MSG_SEND_PROOF = 2;
  /**
   * Command to the service to define the network that is working in
   */
  int MSG_SET_NETWORK = 3;
  /**
   * Command to the service to stop the PoA process on the wallet side
   */
  int MSG_STOP_PROCESS = 4;

  void handle(Message message);
}
