package com.appcoins.net;

public class AppcoinsClient implements AppcoinsConnection {

  private final AppcoinsHTTPClient appcoinsHTTPCLient;

  public AppcoinsClient(AppcoinsHTTPClient appcoinsHTTPCLient) {
    this.appcoinsHTTPCLient = appcoinsHTTPCLient;
  }

  @Override public String getCampaign(QueryParams queryParams) {
    GetCampaignOperation getCampaignOperation = new GetCampaignOperation();

    String response = appcoinsHTTPCLient.Get(getCampaignOperation.mapParams(queryParams));

    return getCampaignOperation.mapResponse(response);
  }
}
