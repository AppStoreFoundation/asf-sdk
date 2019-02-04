package com.appcoins.sdk.billing;

import java.util.Collections;
import java.util.HashMap;

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

  @Override public void querySkuDetailsAsync(SkuDetailsParams skuDetailsParams,
      SkuDetailsResponseListener onSkuDetailsResponseListener) {
    SkuDetailsAsync skuDetailsAsync =
        new SkuDetailsAsync(skuDetailsParams, onSkuDetailsResponseListener, repository);
    skuDetailsAsync.run();
  }

  @Override public void consumeAsync(String purchaseToken, ConsumeResponseListener listener) {
    ConsumeAsync consumeAsync = new ConsumeAsync(purchaseToken, listener, repository);
    consumeAsync.run();
  }

  @Override
  public HashMap<String, Object> launchBillingFlow(BillingFlowParams params, String payload)
      throws ServiceConnectionException {
    try {

      HashMap<String, Object> result =
          repository.launchBillingFlow(params.getSkuType(), params.getSku(), payload);

      return result;
    } catch (ServiceConnectionException e) {
      e.printStackTrace();
      throw new ServiceConnectionException(e.getMessage());
    }
  }
}
