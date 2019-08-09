package com.appcoins.sdk.billing;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.appcoins.sdk.billing.helpers.Utils;
import com.appcoins.sdk.billing.helpers.WalletUtils;
import java.util.ArrayList;

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