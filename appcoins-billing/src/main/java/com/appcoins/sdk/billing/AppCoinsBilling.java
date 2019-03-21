package com.appcoins.sdk.billing;

import java.util.ArrayList;
import java.util.Collections;

public class AppCoinsBilling implements Billing {
  private final Repository repository;
  private final byte[] base64DecodedPublicKey;

  public AppCoinsBilling(Repository repository, byte[] base64DecodedPublicKey) {
    this.repository = repository;
    this.base64DecodedPublicKey = base64DecodedPublicKey;
  }

  @Override public PurchasesResult queryPurchases(String skuType) {
    try {
      PurchasesResult purchasesResult = repository.getPurchases(skuType);
      ArrayList<Purchase> invalidPurchase = new ArrayList<Purchase>();
      for (Purchase purchase : purchasesResult.getPurchases()) {
        String purchaseData = purchase.getOriginalJson();
        byte[] decodeSignature = purchase.getSignature();

        if (!Security.verifyPurchase(base64DecodedPublicKey, purchaseData, decodeSignature)) {
          invalidPurchase.add(purchase);
          return new PurchasesResult(Collections.emptyList(), ResponseCode.ERROR.getValue());
        }
      }

      if (invalidPurchase.size() > 0) {
        purchasesResult.getPurchases()
            .removeAll(invalidPurchase);
      }
      return purchasesResult;
    } catch (ServiceConnectionException e) {
      return new PurchasesResult(Collections.emptyList(), ResponseCode.SERVICE_UNAVAILABLE.getValue());
    }
  }

  @Override public void querySkuDetailsAsync(SkuDetailsParams skuDetailsParams,
      SkuDetailsResponseListener onSkuDetailsResponseListener) {
    SkuDetailsAsync skuDetailsAsync =
        new SkuDetailsAsync(skuDetailsParams, onSkuDetailsResponseListener, repository);
    Thread t = new Thread(skuDetailsAsync);
    t.start();
  }

  @Override public void consumeAsync(String purchaseToken, ConsumeResponseListener listener) {
    ConsumeAsync consumeAsync = new ConsumeAsync(purchaseToken, listener, repository);
    Thread t = new Thread(consumeAsync);
    t.start();
  }

  @Override
  public LaunchBillingFlowResult launchBillingFlow(BillingFlowParams params, String payload)
      throws ServiceConnectionException {
    try {

      LaunchBillingFlowResult result =
          repository.launchBillingFlow(params.getSkuType(), params.getSku(), payload);

      return result;
    } catch (ServiceConnectionException e) {
      e.printStackTrace();
      throw new ServiceConnectionException(e.getMessage());
    }
  }

  @Override public boolean isReady() {
    return repository.isReady();
  }
}
