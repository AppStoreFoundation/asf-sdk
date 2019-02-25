package com.asf.appcoins.sdk.ads.net.responses;

import com.asf.appcoins.sdk.ads.net.AppCoinsClientResponse;
import com.asf.appcoins.sdk.ads.net.ClientResponseHandler;
import com.asf.appcoins.sdk.ads.net.listeners.GetCampaignResponseListener;

public class GetCampaignResponse implements ClientResponseHandler {
  private GetCampaignResponseListener getCampaignResponseListener;

  public GetCampaignResponse(GetCampaignResponseListener getCampaignResponseListener) {

    this.getCampaignResponseListener = getCampaignResponseListener;
  }

  @Override public void clientResponseHandler(AppCoinsClientResponse appcoinsClientResponse) {
    getCampaignResponseListener.responseGetCampaign(appcoinsClientResponse);
  }
}
