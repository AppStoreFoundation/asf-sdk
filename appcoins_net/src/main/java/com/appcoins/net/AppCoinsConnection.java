package com.appcoins.net;

interface AppCoinsConnection {

  void getCampaign(QueryParams queryParams, ClientResponseHandler getCampaignResponseHandler);

  void checkConnectivity(ClientResponseHandler clientResponseHandler);
}
