package com.appcoins.net;

public class GetCampaignOperation extends Operation {

  public static final String PACKAGE_NAME = "packageName";
  public static final String VER_CODE = "vercode";
  public static final String COUNTRY_CODE = "countryCode";
  public static final String SORT = "sort";
  public static final String BY = "by";
  public static final String VALID = "valid";
  public static final String TYPE = "type";

  @Override public String mapParams(QueryParams queryParams) {
    return "?"
        + PACKAGE_NAME
        + "="
        + queryParams.getPackageName()
        + "&"
        + VER_CODE
        + "="
        + queryParams.getVersionCode()
        + "&"
        + COUNTRY_CODE
        + "="
        + queryParams.getCountryCode()
        + "&"
        + SORT
        + "="
        + queryParams.getSort()
        + "&"
        + BY
        + "="
        + queryParams.getBy()
        + "&"
        + VALID
        + "="
        + queryParams.getValid()
        + "&"
        + TYPE
        + "="
        + queryParams.getType();
  }

  @Override public String mapResponse(String response) {
    //TODO
    return response;
  }
}
