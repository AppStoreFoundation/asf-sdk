package com.appcoins.sdk.android_appcoins_billing;

import android.app.Activity;
import com.appcoins.sdk.billing.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.BillingFlowParams;
import com.appcoins.sdk.billing.ConsumeResponseListener;
import com.appcoins.sdk.billing.PurchasesResult;
import com.appcoins.sdk.billing.SkuDetailsParams;
import com.appcoins.sdk.billing.SkuDetailsResponseListener;

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
