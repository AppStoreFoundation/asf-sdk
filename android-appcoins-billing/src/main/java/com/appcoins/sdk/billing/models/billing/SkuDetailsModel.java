package com.appcoins.sdk.billing.models.billing;

public class SkuDetailsModel {

  private String fiatPrice;
  private String fiatPriceCurrencyCode;
  private String appcPrice;

  public SkuDetailsModel(String fiatPrice, String fiatPriceCurrencyCode, String appcPrice) {
    this.fiatPrice = fiatPrice;
    this.fiatPriceCurrencyCode = fiatPriceCurrencyCode;
    this.appcPrice = appcPrice;
  }

  public String getFiatPrice() {
    return fiatPrice;
  }

  public String getFiatPriceCurrencyCode() {
    return fiatPriceCurrencyCode;
  }

  public String getAppcPrice() {
    return appcPrice;
  }
}
