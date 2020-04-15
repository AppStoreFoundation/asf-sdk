package com.appcoins.sdk.billing.models.billing;

public class PurchaseModel {

  private final SkuPurchase purchase;
  private final boolean hasError;

  public PurchaseModel(SkuPurchase purchase, boolean hasError) {

    this.purchase = purchase;
    this.hasError = hasError;
  }

  private PurchaseModel() {
    this.purchase = null;
    this.hasError = true;
  }

  public static PurchaseModel createErrorPurchaseModel() {
    return new PurchaseModel();
  }

  public SkuPurchase getPurchase() {
    return purchase;
  }

  public boolean hasError() {
    return hasError;
  }
}
