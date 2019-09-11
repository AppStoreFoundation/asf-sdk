package com.appcoins.sdk.billing;

public class BuyItemProperties {

  private int apiVersion;
  private String packageName;
  private String sku;
  private String type;
  private String developerPayload;

  public BuyItemProperties(int apiVersion, String packageName, String sku, String type,
      String developerPayload) {
    this.apiVersion = apiVersion;
    this.packageName = packageName;
    this.sku = sku;
    this.type = type;
    this.developerPayload = developerPayload;
  }

  public String getDeveloperPayload() {
    return developerPayload;
  }

  public int getApiVersion() {
    return apiVersion;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getSku() {
    return sku;
  }

  public String getType() {
    return type;
  }
}
