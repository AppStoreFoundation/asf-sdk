package com.appcoins.sdk.android_appcoins_billing;

import android.content.Context;

import com.appcoins.sdk.billing.CatapultAppCoinsBillingLogic;

public class CatapultBillingAppcoinsFactory {

    public static CatapultAppcoinsBilling BuildAppcoinsBilling(Context ctx){
        WalletCommunication walletCommunication = new WalletCommunication(ctx);
        walletCommunication.startService();
        return new CatapultAppcoinsBilling(walletCommunication);
    }

}
