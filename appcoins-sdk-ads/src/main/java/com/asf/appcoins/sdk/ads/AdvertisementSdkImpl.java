package com.asf.appcoins.sdk.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.asf.appcoins.sdk.ads.poa.PoAServiceConnector;

import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_SEND_PROOF;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */

final class AdvertisementSdkImpl implements AdvertisementSdk {

  private final PoAServiceConnector poaConnector;

  AdvertisementSdkImpl(PoAServiceConnector poaConnector) {
    this.poaConnector = poaConnector;
  }


  @Override public void handshake(Context context) {
    poaConnector.startHandshake(context);
  }

  @Override public void sendProof(Context context) {
    if (poaConnector.connectToService(context)) {
      poaConnector.sendMessage(context, MSG_SEND_PROOF, new Bundle());
    }
  }
}
