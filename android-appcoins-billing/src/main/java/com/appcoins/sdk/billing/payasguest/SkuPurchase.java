package com.appcoins.sdk.billing.payasguest;

public class SkuPurchase {

  private final String uid;
  private final RemoteProduct product;
  private final String status;
  private final String packageName;
  private final Signature signature;

  public SkuPurchase(String uid, RemoteProduct product, String status, String packageName,
      Signature signature) {

    this.uid = uid;
    this.product = product;
    this.status = status;
    this.packageName = packageName;
    this.signature = signature;
  }

  public String getUid() {
    return uid;
  }

  public RemoteProduct getProduct() {
    return product;
  }

  public String getStatus() {
    return status;
  }

  public String getPackageName() {
    return packageName;
  }

  public Signature getSignature() {
    return signature;
  }
}
