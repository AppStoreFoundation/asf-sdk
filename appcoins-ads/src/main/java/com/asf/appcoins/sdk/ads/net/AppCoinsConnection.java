package com.asf.appcoins.sdk.ads.net;

interface AppCoinsConnection {

  void getCampaign(QueryParams queryParams, ClientResponseHandler getCampaignResponseHandler);

  void checkConnectivity(ClientResponseHandler clientResponseHandler);
}
