package com.asf.appcoins.sdk.ads.net.responses;

public class ConnectivityResponseHandler implements GetResponseHandler {
  private ClientResponseHandler clientResponseHandler;

  public ConnectivityResponseHandler(ClientResponseHandler clientResponseHandler) {
    this.clientResponseHandler = clientResponseHandler;
  }

  @Override public void getResponseHandler(Object response) {
    AppCoinsClientResponse appcoinsClientResponse =
        new AppCoinsClientResponsePing((boolean) response);
    clientResponseHandler.clientResponseHandler(appcoinsClientResponse);
  }
}
