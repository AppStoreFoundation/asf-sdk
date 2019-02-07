package com.appcoins.net;

public class AppcoinsClient implements AppcoinsConnection {

  private final AppcoinsHTTPClient appcoinsHTTPCLient;

  public AppcoinsClient(AppcoinsHTTPClient appcoinsHTTPCLient) {
    this.appcoinsHTTPCLient = appcoinsHTTPCLient;
  }

  @Override
  public void getCampaign(String packageName, int versionCode, String countryCode, String sort,
      String by, boolean valid, String type) {

    QueryParams queryParams =
        new QueryParams(packageName, Integer.toString(versionCode), countryCode, sort, by,
            Boolean.toString(valid), type);

    appcoinsHTTPCLient.Get(queryParams);

  }
}
