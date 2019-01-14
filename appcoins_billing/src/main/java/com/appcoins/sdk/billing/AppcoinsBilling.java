package com.appcoins.sdk.billing;

public interface AppcoinsBilling {

    void querySkuDetails(String sku);

    void queryPurchases(String skuType);

    void querySkuDetailsAsync(String sku);

}
