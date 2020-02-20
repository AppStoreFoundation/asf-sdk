package com.appcoins.sdk.billing;

import com.appcoins.sdk.billing.exceptions.ServiceConnectionException;
import com.appcoins.sdk.billing.listeners.ConsumeResponseListener;
import com.appcoins.sdk.billing.listeners.SkuDetailsResponseListener;

public interface Billing {

  PurchasesResult queryPurchases(String skuType);

  void querySkuDetailsAsync(SkuDetailsParams skuDetailsParams,
      SkuDetailsResponseListener onSkuDetailsResponseListener);

  void consumeAsync(String purchaseToken, ConsumeResponseListener listener);

  LaunchBillingFlowResult launchBillingFlow(BillingFlowParams params, String payload)
      throws ServiceConnectionException;

  boolean isReady();

  boolean verifyPurchase(String purchaseData, byte[] decodeSignature);
}

