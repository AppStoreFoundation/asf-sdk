package com.appcoins.sdk.android_appcoins_billing;

import com.appcoins.sdk.billing.AppcoinsBilling;
import com.appcoins.sdk.billing.CatapultAppCoinsBillingLogic;


public class CatapultAppcoinsBilling implements AppcoinsBilling {


    private IabHelper iabHelper;

    private CatapultAppCoinsBillingLogic catapultAppCoinsBillingLogic;

    public CatapultAppcoinsBilling(IabHelper iabHelper){
        this.iabHelper = iabHelper;
        catapultAppCoinsBillingLogic = new CatapultAppCoinsBillingLogic();
    }


    @Override
    public void querySkuDetails(String sku) {

    }

    public void startService(final OnIabSetupFinishedListener listener){
        iabHelper.startService(listener);
    }
}
