package com.asf.appcoins.sdk.ads;

import android.app.Activity;
import android.os.Bundle;

import com.asf.appcoins.sdk.ads.poa.PoAServiceConnector;

import static com.asf.appcoins.sdk.ads.poa.PoAServiceConnector.MSG_SEND_PROOF;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */

final class AdvertisementSdkImpl implements AdvertisementSdk {

  private final PoAServiceConnector poaConnector;

  AdvertisementSdkImpl(PoAServiceConnector poaConnector) {
    this.poaConnector = poaConnector;
  }


  @Override public void handshake(Activity activity) {
    poaConnector.startHandshake(activity);
  }

  @Override public void sendProof(Activity activity) {
    poaConnector.sendMessage(activity, MSG_SEND_PROOF, new Bundle());
  }
}
