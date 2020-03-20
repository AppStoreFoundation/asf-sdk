package com.appcoins.sdk.billing.listeners;

import com.appcoins.sdk.billing.models.billing.SkuPurchase;
import java.util.ArrayList;
import java.util.List;

public class PurchasesModel {

  private final List<SkuPurchase> skuPurchases;
  private final boolean error;

  public PurchasesModel(List<SkuPurchase> skuPurchases, boolean error) {
    this.skuPurchases = skuPurchases;
    this.error = error;
  }

  public PurchasesModel() {
    this.skuPurchases = new ArrayList<>();
    this.error = true;
  }

  public List<SkuPurchase> getSkuPurchases() {
    return skuPurchases;
  }

  public boolean hasError() {
    return error;
  }
}
