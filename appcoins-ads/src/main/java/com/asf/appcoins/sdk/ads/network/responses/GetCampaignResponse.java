package com.asf.appcoins.sdk.ads.network.responses;

import com.asf.appcoins.sdk.ads.network.listeners.GetCampaignResponseListener;

public class GetCampaignResponse implements ClientResponseHandler {
  private GetCampaignResponseListener getCampaignResponseListener;

  public GetCampaignResponse(GetCampaignResponseListener getCampaignResponseListener) {

    this.getCampaignResponseListener = getCampaignResponseListener;
  }

  @Override public void clientResponseHandler(AppCoinsClientResponse appcoinsClientResponse) {
    getCampaignResponseListener.responseGetCampaign(appcoinsClientResponse);
  }
}
