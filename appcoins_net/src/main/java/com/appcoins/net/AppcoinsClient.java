package com.appcoins.net;

public class AppcoinsClient implements AppcoinsConnection {

  private final String packageName;
  private final int versionCode;
  private String serviceUrl;

  public AppcoinsClient(String packageName, int versionCode, String serviceUrl) {
    this.packageName = packageName;
    this.versionCode = versionCode;
    this.serviceUrl = serviceUrl;
  }

  @Override public void getCampaign(QueryParams queryParams,
      ClientResponseHandler clientResponseHandler) {
    GetCampaignOperation getCampaignOperation = new GetCampaignOperation();

    AppcoinsHTTPClient appcoinsHTTPClient =
        new AppcoinsHTTPClient(serviceUrl, getCampaignOperation.mapParams(packageName,Integer.toString(versionCode),queryParams),
            response -> {
              AppcoinsClientResponse appcoinsClientResponse =
                  getCampaignOperation.mapResponse(response);
              clientResponseHandler.clientResponseHandler(appcoinsClientResponse);
            });

    Thread operation = new Thread(appcoinsHTTPClient);
    operation.start();
  }

  @Override public boolean checkConnectivity() {
    //TODO
    return true;
  }

  @Override public boolean checkNetworkAvailable() {
    //TODO
    return true;
  }
}
