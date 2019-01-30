package com.appcoins.sdk.billing;

import java.util.HashMap;

public interface Billing {

    PurchasesResult queryPurchases(String skuType);

    void querySkuDetailsAsync(SkuDetailsParams skuDetailsParams, SkuDetailsResponseListener onSkuDetailsResponseListener);

    void consumeAsync(String purchaseToken, ConsumeResponseListener listener);

    HashMap<String, Object> launchBillingFlow(BillingFlowParams params)
        throws ServiceConnectionException;
}

