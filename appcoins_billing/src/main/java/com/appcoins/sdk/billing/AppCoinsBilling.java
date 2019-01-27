package com.appcoins.sdk.billing;

import java.util.Collections;
import java.util.List;

public class AppCoinsBilling implements Billing {
  private final Repository repository;

  public AppCoinsBilling(Repository repository) {
    this.repository = repository;
  }

  @Override public PurchasesResult queryPurchases(String skuType) {
    try {
      return repository.getPurchases(skuType);
    } catch (ServiceConnectionException e) {
      e.printStackTrace();
      return new PurchasesResult(Collections.emptyList(), -1);
    }
  }

  @Override public void querySkuDetailsAsync(SkuDetailsParam skuDetailsParam,
      ResponseListener onSkuDetailsResponseListener) {

  }

  @Override
  public void launchPurchaseFlow(Object act, String sku, String itemType, List<String> oldSkus,
      int requestCode, ResponseListener listener, String extraData) {

  }
}
