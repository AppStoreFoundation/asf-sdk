package com.asf.appcoins.sdk.ads;

import android.app.Activity;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */

public interface AdvertisementSdk {

  void handshake(Activity activity);

  void sendProof(Activity activity);

}
