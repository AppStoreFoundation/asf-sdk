package com.asf.appcoins.sdk.ads.network;

import com.asf.appcoins.sdk.ads.network.responses.ClientResponseHandler;
import com.asf.appcoins.sdk.ads.network.responses.ConnectivityResponseHandler;
import com.asf.appcoins.sdk.ads.network.responses.HTTPResponseHandler;
import com.asf.appcoins.sdk.ads.network.clients.GetCampaignHTTPClient;
import com.asf.appcoins.sdk.ads.network.clients.CheckConnectionCampaignClient;

public class AppCoinsClient {

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

  public void getCampaign(QueryParams queryParams, ClientResponseHandler clientResponseHandler) {
    HTTPResponseHandler httpResponseHandler = new HTTPResponseHandler(clientResponseHandler);
    GetCampaignHTTPClient appcoinsHTTPClient =
        new GetCampaignHTTPClient(packageName, versionCode, serviceUrl, interceptor, queryParams,
            httpResponseHandler);

    Thread operation = new Thread(appcoinsHTTPClient);
    operation.start();
  }

  public void checkConnectivity(ClientResponseHandler clientResponseHandler) {
    ConnectivityResponseHandler connectivityResponseHandler =
        new ConnectivityResponseHandler(clientResponseHandler);
    GetCampaignHTTPClient appcoinsHTTPClient =
        new CheckConnectionCampaignClient(packageName, versionCode, serviceUrl, interceptor,
            connectivityResponseHandler);

    Thread operation = new Thread(appcoinsHTTPClient);
    operation.start();
  }
}
