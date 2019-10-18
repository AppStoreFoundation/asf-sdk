package com.asf.appcoins.sdk.ads;

import android.content.Context;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnector;
import com.asf.appcoins.sdk.ads.poa.PoAServiceConnectorImpl;

import static com.asf.appcoins.sdk.ads.AppCoinsAds.NETWORK_MAIN;
import static com.asf.appcoins.sdk.ads.AppCoinsAds.NETWORK_ROPSTEN;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */
public final class AppCoinsAdsBuilder {

  private static final String TAG = AppCoinsAdsBuilder.class.getSimpleName();

  private PoAServiceConnector poaConnector;
  private String country;
  private boolean debug;

  public AppCoinsAdsBuilder withDebug(boolean debug) {
    this.debug = debug;
    return this;
  }

  public AppCoinsAds createAdvertisementSdk(Context context) {

    if (country == null) {
      country = context.getResources()
          .getConfiguration().locale.getDisplayCountry();
    }

    int networkId;
    if (debug) {
      networkId = NETWORK_ROPSTEN;
    } else {
      networkId = NETWORK_MAIN;
    }

    if (this.poaConnector == null) {
      this.poaConnector = new PoAServiceConnectorImpl(null,networkId);
    }

    return new AppCoinsAdsImpl(poaConnector, networkId);
  }
}