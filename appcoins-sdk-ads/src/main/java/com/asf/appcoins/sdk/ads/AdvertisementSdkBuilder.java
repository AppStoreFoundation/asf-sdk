package com.asf.appcoins.sdk.ads;

import android.content.Context;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnector;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnectorImpl;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */
public final class AdvertisementSdkBuilder {

  private PoAServiceConnector poaConnector;
  private String country;

  public AdvertisementSdk createAdvertisementSdk(Context context) {
    if (this.poaConnector == null) {
      this.poaConnector = new PoAServiceConnectorImpl(null);
    }

    if (country == null) {
      country = context.getResources()
          .getConfiguration().locale.getDisplayCountry();
    }

    return new AdvertisementSdkImpl(poaConnector);
  }
}