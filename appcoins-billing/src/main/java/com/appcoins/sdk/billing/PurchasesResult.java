package com.appcoins.sdk.billing;

import java.util.List;

public class PurchasesResult {

  private final List<Purchase> purchases;
  private final int responseCode;

  public PurchasesResult(List<Purchase> purchases, int responseCode) {
    this.purchases = purchases;
    this.responseCode = responseCode;
  }

  public List<Purchase> getPurchases() {
    return purchases;
  }

  public int getResponseCode() {
    return responseCode;
  }
}
