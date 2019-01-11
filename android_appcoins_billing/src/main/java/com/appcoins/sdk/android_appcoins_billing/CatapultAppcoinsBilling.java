package com.appcoins.sdk.android_appcoins_billing;

import com.appcoins.sdk.billing.AppcoinsBilling;
import com.appcoins.sdk.billing.CatapultAppCoinsBillingLogic;


public class CatapultAppcoinsBilling implements AppcoinsBilling {


    private WalletCommunication walletCommunication;

    private CatapultAppCoinsBillingLogic catapultAppCoinsBillingLogic;

    public CatapultAppcoinsBilling(WalletCommunication walletCommunication){
        this.walletCommunication = walletCommunication;
        catapultAppCoinsBillingLogic = new CatapultAppCoinsBillingLogic();
    }


    @Override
    public void querySkuDetails(String sku) {

    }
}
