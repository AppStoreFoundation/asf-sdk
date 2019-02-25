package com.asf.appcoins.sdk.ads.net;

import com.asf.appcoins.sdk.ads.net.responses.ConnectivityResponseHandler;
import com.asf.appcoins.sdk.ads.net.responses.HTTPResponseHandler;
import com.asf.appcoins.sdk.ads.net.threads.HTTPClient;
import com.asf.appcoins.sdk.ads.net.threads.PingClient;

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

    HTTPResponseHandler httpResponseHandler =
        new HTTPResponseHandler(getCampaignOperation, clientResponseHandler);
    HTTPClient appcoinsHTTPClient = new HTTPClient(serviceUrl, interceptor,
        getCampaignOperation.mapParams(packageName, Integer.toString(versionCode), queryParams),
        httpResponseHandler);

    Thread operation = new Thread(appcoinsHTTPClient);
    operation.start();
  }

  @Override public void checkConnectivity(ClientResponseHandler clientResponseHandler) {
    String pathUrl = GetCampaignOperation.getRequestCampaignPath();
    ConnectivityResponseHandler connectivityResponseHandler = new ConnectivityResponseHandler(clientResponseHandler);
    HTTPClient appcoinsHTTPClient = new PingClient(serviceUrl + pathUrl, interceptor, connectivityResponseHandler);
    Thread operation = new Thread(appcoinsHTTPClient);
    operation.start();
  }
}
