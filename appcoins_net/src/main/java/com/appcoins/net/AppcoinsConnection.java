package com.appcoins.net;

interface AppcoinsConnection {

  void getCampaign(QueryParams queryParams, ClientResponseHandler getCampaignResponseHandler);

  boolean checkConnectivity();

  boolean checkNetworkAvailable();
}
