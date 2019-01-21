package com.appcoins.sdk.billing;

public interface AppcoinsBilling {

    void querySkuDetails(String sku);

    PurchasesResult queryPurchases(String skuType);

    PurchasesResult consumePurchase (Purchase purchase);

    void querySkuDetailsAsync(String sku);

}

