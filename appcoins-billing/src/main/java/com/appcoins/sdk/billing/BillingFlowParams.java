package com.appcoins.sdk.billing;

public class BillingFlowParams {

  private String orderReference;

  private String developerPayload;

  private String origin;

  private final String sku;

  private final String skuType;

  public BillingFlowParams(String sku, String skuType, String orderReference,
      String developerPayload, String origin) {
    this.sku = sku;
    this.skuType = skuType;
    this.orderReference = orderReference;
    this.developerPayload = developerPayload;
    this.origin = origin;
  }

  public String getSku() {
    return sku;
  }

  public String getSkuType() {
    return skuType;
  }

  public String getOrderReference() {
    return orderReference;
  }

  public String getDeveloperPayload() {
    return developerPayload;
  }

  public String getOrigin() {
    return origin;
  }
}
