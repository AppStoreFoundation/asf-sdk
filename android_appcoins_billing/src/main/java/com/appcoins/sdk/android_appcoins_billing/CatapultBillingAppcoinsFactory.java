package com.appcoins.sdk.android_appcoins_billing;

import android.content.Context;

public class CatapultBillingAppcoinsFactory {

    public static CatapultAppcoinsBilling BuildAppcoinsBilling(Context ctx){
        IabHelper iabHelper = new IabHelper(ctx);
        return new CatapultAppcoinsBilling(iabHelper);
    }

}
