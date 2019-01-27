package com.appcoins.sdk.billing;

/**
 * Represents an in-app billing purchase.
 */
public class Purchase {
  private final String itemType;  // ITEM_TYPE_INAPP or ITEM_TYPE_SUBS
  private final String orderId;
  private final String packageName;
  private final String sku;
  private final long purchaseTime;
  private final int purchaseState;
  private final String developerPayload;
  private final String token;
  private final String originalJson;
  private final String signature;
  private final boolean isAutoRenewing;

  public Purchase(String orderId, String itemType, String originalJson, String signature,
      long purchaseTime, int purchaseState, String developerPayload, String token,
      String packageName, String sku, boolean isAutoRenewing) {
    this.itemType = itemType;
    this.orderId = orderId;
    this.packageName = packageName;
    this.sku = sku;
    this.purchaseTime = purchaseTime;
    this.purchaseState = purchaseState;
    this.developerPayload = developerPayload;
    this.token = token;
    this.originalJson = originalJson;
    this.signature = signature;
    this.isAutoRenewing = isAutoRenewing;
  }

  public String getItemType() {
    return itemType;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getSku() {
    return sku;
  }

  public long getPurchaseTime() {
    return purchaseTime;
  }

  public int getPurchaseState() {
    return purchaseState;
  }

  public String getDeveloperPayload() {
    return developerPayload;
  }

  public String getToken() {
    return token;
  }

  public String getOriginalJson() {
    return originalJson;
  }

  public String getSignature() {
    return signature;
  }

  public boolean isAutoRenewing() {
    return isAutoRenewing;
  }
}
