package com.appcoins.net;

public class AppcoinsClient implements AppcoinsConnection {

  private final String packageName;
  private final int versionCode;
  private final Interceptor interceptor;
  private String serviceUrl;

  public AppcoinsClient(String packageName, int versionCode, String serviceUrl,
      Interceptor interceptor) {
    this.packageName = packageName;
    this.versionCode = versionCode;
    this.serviceUrl = serviceUrl;
    this.interceptor = interceptor;
  }

  @Override
  public void getCampaign(QueryParams queryParams, ClientResponseHandler clientResponseHandler) {
    GetCampaignOperation getCampaignOperation = new GetCampaignOperation();

    AppcoinsHTTPClient appcoinsHTTPClient = new AppcoinsHTTPClient(serviceUrl, interceptor,
        getCampaignOperation.mapParams(packageName, Integer.toString(versionCode), queryParams),
        AppcoinsHTTPClient.GET_CONNECTION, response -> {

      AppcoinsClientResponse appcoinsClientResponse =
          getCampaignOperation.mapResponse((String) response);
      clientResponseHandler.clientResponseHandler(appcoinsClientResponse);

    });

    Thread operation = new Thread(appcoinsHTTPClient);
    operation.start();
  }

  @Override public void checkConnectivity(ClientResponseHandler clientResponseHandler) {
    AppcoinsHTTPClient appcoinsHTTPClient =
        new AppcoinsHTTPClient(serviceUrl, interceptor, null, AppcoinsHTTPClient.PING_CONNECTION,
            response -> {

              AppcoinsClientResponse appcoinsClientResponse =
                  new AppcoinsClientResponsePing((boolean) response);
              clientResponseHandler.clientResponseHandler(appcoinsClientResponse);

        });
    Thread operation = new Thread(appcoinsHTTPClient);
    operation.start();
  }
}
