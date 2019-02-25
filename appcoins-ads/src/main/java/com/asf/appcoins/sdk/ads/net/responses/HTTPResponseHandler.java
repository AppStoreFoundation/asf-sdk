package com.asf.appcoins.sdk.ads.net.responses;

import com.asf.appcoins.sdk.ads.net.AppCoinsClientResponse;
import com.asf.appcoins.sdk.ads.net.ClientResponseHandler;
import com.asf.appcoins.sdk.ads.net.GetCampaignOperation;
import com.asf.appcoins.sdk.ads.net.GetResponseHandler;

public class HTTPResponseHandler implements GetResponseHandler {
  private GetCampaignOperation getCampaignOperation;
  private ClientResponseHandler clientResponseHandler;

  public HTTPResponseHandler(GetCampaignOperation getCampaignOperation,
      ClientResponseHandler clientResponseHandler) {
    this.getCampaignOperation = getCampaignOperation;
    this.clientResponseHandler = clientResponseHandler;
  }

  @Override public void getResponseHandler(Object response) {
    AppCoinsClientResponse appcoinsClientResponse =
        getCampaignOperation.mapResponse((String) response);
    clientResponseHandler.clientResponseHandler(appcoinsClientResponse);
  }
}
