package com.appcoins.sdk.billing;

import android.app.Activity;

public interface AppcoinsBillingClient {
  PurchasesResult queryPurchases(String skuType);

  void querySkuDetailsAsync(SkuDetailsParams skuDetailsParams,
      SkuDetailsResponseListener onSkuDetailsResponseListener);

  void consumeAsync(String token, ConsumeResponseListener consumeResponseListener);

  int launchBillingFlow(Activity activity, BillingFlowParams billingFlowParams);

  void startConnection(AppCoinsBillingStateListener listener);

  void endConnection();

  boolean isReady();
}
