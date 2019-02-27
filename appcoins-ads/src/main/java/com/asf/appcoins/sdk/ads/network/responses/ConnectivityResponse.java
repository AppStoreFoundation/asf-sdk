package com.asf.appcoins.sdk.ads.network.responses;

import com.asf.appcoins.sdk.ads.network.listeners.CheckConnectivityResponseListener;

public class ConnectivityResponse implements ClientResponseHandler {

  private CheckConnectivityResponseListener connectivityResponseListener;

  public ConnectivityResponse(CheckConnectivityResponseListener connectivityResponseListener) {

    this.connectivityResponseListener = connectivityResponseListener;
  }

  @Override public void clientResponseHandler(AppCoinsClientResponse appcoinsClientResponse) {
    connectivityResponseListener.responseConnectivity(
        ((AppCoinsClientResponsePing) appcoinsClientResponse).HasConnection());
  }
}
