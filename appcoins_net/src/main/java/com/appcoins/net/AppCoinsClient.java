package com.appcoins.net;

public class AppCoinsClient implements AppCoinsConnection {

  private final String packageName;
  private final int versionCode;
  private final Interceptor interceptor;
  private final String serviceUrl;

  public AppCoinsClient(String packageName, int versionCode, String serviceUrl,
      Interceptor interceptor) {
    this.packageName = packageName;
    this.versionCode = versionCode;
    this.serviceUrl = serviceUrl;
    this.interceptor = interceptor;
  }

  @Override
  public void getCampaign(QueryParams queryParams, ClientResponseHandler clientResponseHandler) {
    GetCampaignOperation getCampaignOperation = new GetCampaignOperation();

    AppCoinsHTTPClient appcoinsHTTPClient = new AppCoinsHTTPClient(serviceUrl, interceptor,
        getCampaignOperation.mapParams(packageName, Integer.toString(versionCode), queryParams),
        response -> {

          AppCoinsClientResponse appcoinsClientResponse =
              getCampaignOperation.mapResponse((String) response);
          clientResponseHandler.clientResponseHandler(appcoinsClientResponse);
        });

    Thread operation = new Thread(appcoinsHTTPClient);
    operation.start();
  }

  @Override public void checkConnectivity(ClientResponseHandler clientResponseHandler) {
    String pathUrl = GetCampaignOperation.getRequestCampaignPath();
    AppCoinsHTTPClient appcoinsHTTPClient =
        new AppCoinsPingClient(serviceUrl + pathUrl, interceptor, response -> {

          AppCoinsClientResponse appcoinsClientResponse =
              new AppCoinsClientResponsePing((boolean) response);
          clientResponseHandler.clientResponseHandler(appcoinsClientResponse);
        });
    Thread operation = new Thread(appcoinsHTTPClient);
    operation.start();
  }
}
