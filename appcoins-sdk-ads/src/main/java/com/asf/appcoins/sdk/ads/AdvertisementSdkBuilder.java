package com.asf.appcoins.sdk.ads;

import com.asf.appcoins.sdk.ads.poa.PoAServiceConnector;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnectorImpl;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */
public final class AdvertisementSdkBuilder {

  private PoAServiceConnector poaConnector;

  public AdvertisementSdk createAdvertisementSdk() {
    if (this.poaConnector == null) {
      this.poaConnector = PoAServiceConnectorImpl.getInstance();
    }

    return new AdvertisementSdkImpl(poaConnector);
  }
}