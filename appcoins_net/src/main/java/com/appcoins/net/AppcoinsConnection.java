package com.appcoins.net;

interface AppcoinsConnection {

  void getCampaign(String packageName,int versionCode,
      String countryCode, String sort,
      String by, boolean valid,String type);
}
