package com.appcoins.sdk.billing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import sun.rmi.runtime.Log;

public class AppCoinsBilling implements Billing {
  private final Repository repository;
  private final String base64PublicKey;

  public AppCoinsBilling(Repository repository, String base64PublicKey) {
    this.repository = repository;
    this.base64PublicKey = base64PublicKey;
  }

  @Override public PurchasesResult queryPurchases(String skuType) {
    try {
      PurchasesResult pr = repository.getPurchases(skuType);
      ArrayList<Purchase> invalidPurchase = new ArrayList<Purchase>();

      for (Purchase purchase : pr.getPurchases()) {
        String purchaseData = purchase.getOriginalJson();
        String signature = purchase.getSignature();

        try {
          if (!Security.verifyPurchase(base64PublicKey, purchaseData, signature)) {
            invalidPurchase.add(purchase);
          }
        } catch (IllegalArgumentException e) {
          invalidPurchase.add(purchase);
        }
      }

      if (invalidPurchase.size() > 0) {
        pr.getPurchases()
            .removeAll(invalidPurchase);
      }

      return pr;
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
