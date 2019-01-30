package com.appcoins.sdk.billing;

public class BillingFlowParams {

  private final String sku;

  private final String skuType;

  private final int requestCode;

  private final String payload;

  public BillingFlowParams(String sku, String skuType, int requestCode, String payload) {
    this.sku = sku;
    this.skuType = skuType;
    this.requestCode = requestCode;
    this.payload = payload;
  }

  public String getSku() {
    return sku;
  }

  public String getSkuType() {
    return skuType;
  }

  public int getRequestCode() {
    return requestCode;
  }

  public String getPayload() {
    return payload;
  }
}
