package com.appcoins.sdk.billing.models.billing;

public class SkuDetailsModel {

  private String fiatPrice;
  private String fiatPriceCurrencyCode;
  private String appcPrice;
  private String sku;

  public SkuDetailsModel(String fiatPrice, String fiatPriceCurrencyCode, String appcPrice,
      String sku) {
    this.fiatPrice = fiatPrice;
    this.fiatPriceCurrencyCode = fiatPriceCurrencyCode;
    this.appcPrice = appcPrice;
    this.sku = sku;
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

  public String getSku() {
    return sku;
  }
}
