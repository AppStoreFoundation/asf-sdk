package com.appcoins.sdk.billing;

public interface AppcoinsBilling {

    void queryPurchases(String skuType);

    void querySkuDetailsAsync(SkuDetailsParam skuDetailsParam , ResponseListener onSkuDetailsResponseListener);

}
