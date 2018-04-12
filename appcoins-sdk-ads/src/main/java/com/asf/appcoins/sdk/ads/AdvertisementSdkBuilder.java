package com.asf.appcoins.sdk.ads;

import android.content.Context;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnector;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnectorImpl;

import static com.asf.appcoins.sdk.ads.AdvertisementSdk.NETWORK_MAIN;
import static com.asf.appcoins.sdk.ads.AdvertisementSdk.NETWORK_ROPSTEN;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */
public final class AdvertisementSdkBuilder {

  private PoAServiceConnector poaConnector;
  private String country;
  private boolean debug;

  public AdvertisementSdkBuilder withDebug(boolean debug) {
    this.debug = debug;
    return this;
  }

  public AdvertisementSdk createAdvertisementSdk(Context context) {
    if (this.poaConnector == null) {
      this.poaConnector = new PoAServiceConnectorImpl(null);
    }

    if (country == null) {
      country = context.getResources()
          .getConfiguration().locale.getDisplayCountry();
    }

    int networkId;
    String contractAddress;
    if (debug) {
      networkId = NETWORK_ROPSTEN;
      contractAddress = "0xab949343e6c369c6b17c7ae302c1debd4b7b61c3";
    } else {
      networkId = NETWORK_MAIN;
      contractAddress = "0xab949343e6c369c6b17c7ae302c1debd4b7b61c3";
    }

    return new AdvertisementSdkImpl(poaConnector, networkId, contractAddress);
  }
}