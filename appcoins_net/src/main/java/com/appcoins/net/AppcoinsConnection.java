package com.appcoins.net;

interface AppcoinsConnection {

  void getCampaign(QueryParams queryParams, ClientResponseHandler getCampaignResponseHandler);

  void checkConnectivity(ClientResponseHandler clientResponseHandler);
}
