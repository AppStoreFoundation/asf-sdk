package com.appcoins.sdk.billing.payasguest;

public class PurchaseModel {

  private final SkuPurchase purchase;
  private final boolean hasError;

  public PurchaseModel(SkuPurchase purchase, boolean hasError) {

    this.purchase = purchase;
    this.hasError = hasError;
  }

  public PurchaseModel() {
    this.purchase = null;
    this.hasError = true;
  }

  public SkuPurchase getPurchase() {
    return purchase;
  }

  public boolean hasError() {
    return hasError;
  }
}
