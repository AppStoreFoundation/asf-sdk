package com.asf.appcoins.sdk.ads.network.clients;

import com.asf.appcoins.sdk.ads.network.Interceptor;
import com.asf.appcoins.sdk.ads.network.QueryParams;
import com.asf.appcoins.sdk.ads.network.responses.CampaignResponseHandler;

public class WalletCampaignService implements Runnable {
  private String packageName;
  private int versionCode;
  private String serviceUrl;
  private Interceptor interceptor;
  private QueryParams queryParams;
  private CampaignResponseHandler campaignResponseHandler;

  public WalletCampaignService(String packageName, int versionCode, String serviceUrl,
      Interceptor interceptor, QueryParams queryParams,
      CampaignResponseHandler campaignResponseHandler) {
    this.packageName = packageName;
    this.versionCode = versionCode;
    this.serviceUrl = serviceUrl;
    this.interceptor = interceptor;
    this.queryParams = queryParams;
    this.campaignResponseHandler = campaignResponseHandler;
  }

  @Override public void run() {

  }
}
