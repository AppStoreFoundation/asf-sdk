package com.appcoins.net;

import java.io.IOException;

public class AppcoinsClient implements AppcoinsConnection {

  private final AppcoinsHTTPClient appcoinsHTTPCLient;

  public AppcoinsClient(AppcoinsHTTPClient appcoinsHTTPCLient) {
    this.appcoinsHTTPCLient = appcoinsHTTPCLient;
  }

  @Override public AppcoinsClientResponse getCampaign(QueryParams queryParams) {
    GetCampaignOperation getCampaignOperation = new GetCampaignOperation();

    String response = null;
    //TODO Response handling
    try {
      response = appcoinsHTTPCLient.Get(getCampaignOperation.mapParams(queryParams));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return getCampaignOperation.mapResponse(response);
  }
}
