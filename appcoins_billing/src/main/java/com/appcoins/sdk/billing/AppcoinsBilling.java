package com.appcoins.sdk.billing;

import java.util.List;

public interface AppcoinsBilling {

    PurchasesResult queryPurchases(String skuType);

    PurchasesResult consumePurchase (Purchase purchase);

    void querySkuDetailsAsync(SkuDetailsParam skuDetailsParam , ResponseListener onSkuDetailsResponseListener);

    void launchPurchaseFlow(Object act,String sku, String itemType, List<String> oldSkus, int requestCode, ResponseListener listener, String extraData);
}

