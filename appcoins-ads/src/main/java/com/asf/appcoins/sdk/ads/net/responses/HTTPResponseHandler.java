package com.asf.appcoins.sdk.ads.net.responses;

public class HTTPResponseHandler implements GetResponseHandler {
  private ClientResponseHandler clientResponseHandler;

  public HTTPResponseHandler(ClientResponseHandler clientResponseHandler) {
    this.clientResponseHandler = clientResponseHandler;
  }

  @Override public void getResponseHandler(Object response) {
    AppCoinsClientResponse appcoinsClientResponse = new AppCoinsClientResponse((String) response);
    clientResponseHandler.clientResponseHandler(appcoinsClientResponse);
  }
}
