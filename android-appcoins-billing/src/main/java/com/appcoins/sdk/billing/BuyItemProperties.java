package com.appcoins.sdk.billing;

import java.io.Serializable;

public class BuyItemProperties implements Serializable {

  private int apiVersion;
  private String packageName;
  private String sku;
  private String type;
  private DeveloperPayload developerPayload;

  public BuyItemProperties(int apiVersion, String packageName, String sku, String type,
      DeveloperPayload developerPayload) {
    this.apiVersion = apiVersion;
    this.packageName = packageName;
    this.sku = sku;
    this.type = type;
    this.developerPayload = developerPayload;
  }

  public DeveloperPayload getDeveloperPayload() {
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
