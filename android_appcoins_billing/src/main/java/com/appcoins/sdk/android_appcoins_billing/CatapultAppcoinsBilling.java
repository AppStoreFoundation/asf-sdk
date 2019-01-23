package com.appcoins.sdk.android_appcoins_billing;

import com.appcoins.sdk.billing.AppcoinsBilling;
import com.appcoins.sdk.billing.Inventory;
import com.appcoins.sdk.billing.Purchase;
import com.appcoins.sdk.billing.PurchasesResult;


public class CatapultAppcoinsBilling implements AppcoinsBilling {


    private IabHelper iabHelper;


    public CatapultAppcoinsBilling(IabHelper iabHelper){
        this.iabHelper = iabHelper;
    }


    @Override
    public void querySkuDetails(String sku) {

    }

    @Override
    public PurchasesResult queryPurchases(String skuType) {
        Inventory inv = new Inventory();
        return this.iabHelper.queryPurchases(inv, skuType);

    }

    @Override
    public PurchasesResult consumePurchase (Purchase purchase){
        //return this.iabHelper.consume(purchase);
        return null;
    }

    @Override
    public void querySkuDetailsAsync(String sku) {

    }

    public void startService(final OnIabSetupFinishedListener listener){
        iabHelper.startService(listener);
    }
}



