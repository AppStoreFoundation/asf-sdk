package com.appcoins.sdk.billing;

public interface Billing {

    PurchasesResult queryPurchases(String skuType);

    void querySkuDetailsAsync(SkuDetailsParams skuDetailsParams, SkuDetailsResponseListener onSkuDetailsResponseListener);

    void consumeAsync(String purchaseToken, ConsumeResponseListener listener);

    LaunchBillingFlowResult launchBillingFlow(BillingFlowParams params, String payload)
        throws ServiceConnectionException;

  boolean isReady();
}

