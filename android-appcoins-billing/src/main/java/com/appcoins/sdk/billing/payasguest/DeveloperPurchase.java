package com.appcoins.sdk.billing.payasguest;

public class DeveloperPurchase {

  private final String orderId;
  private final String packageName;
  private final String productId;
  private final String purchaseTime;
  private final String purchaseToken;
  private final String purchaseState;
  private final String developerPayload;

  public DeveloperPurchase(String orderId, String packageName, String productId,
      String purchaseTime, String purchaseToken, String purchaseState, String developerPayload) {
    this.orderId = orderId;
    this.packageName = packageName;
    this.productId = productId;
    this.purchaseTime = purchaseTime;
    this.purchaseToken = purchaseToken;
    this.purchaseState = purchaseState;
    this.developerPayload = developerPayload;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getProductId() {
    return productId;
  }

  public String getPurchaseTime() {
    return purchaseTime;
  }

  public String getPurchaseToken() {
    return purchaseToken;
  }

  public String getPurchaseState() {
    return purchaseState;
  }

  public String getDeveloperPayload() {
    return developerPayload;
  }
}
