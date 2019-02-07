package com.appcoins.net;

public class AppcoinsClient implements AppcoinsConnection {

  private final AppcoinsHTTPClient appcoinsHTTPCLient;

  public AppcoinsClient(AppcoinsHTTPClient appcoinsHTTPCLient) {
    this.appcoinsHTTPCLient = appcoinsHTTPCLient;
  }

  @Override
  public void getCampaign(QueryParams queryParams) {
    //MapperParams
    appcoinsHTTPCLient.Get(null);
    //MapperResponse
  }
}
