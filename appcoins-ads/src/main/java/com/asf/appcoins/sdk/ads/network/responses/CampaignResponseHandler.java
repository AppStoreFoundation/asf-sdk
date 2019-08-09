package com.asf.appcoins.sdk.ads.network.responses;

public class CampaignResponseHandler implements GetResponseHandler {
  private ClientResponseHandler clientResponseHandler;

  public CampaignResponseHandler(ClientResponseHandler clientResponseHandler) {
    this.clientResponseHandler = clientResponseHandler;
  }

  @Override public void getResponseHandler(Object response) {
    AppCoinsClientResponse appcoinsClientResponse = new AppCoinsClientResponse((String) response);
    clientResponseHandler.clientResponseHandler(appcoinsClientResponse);
  }
}
