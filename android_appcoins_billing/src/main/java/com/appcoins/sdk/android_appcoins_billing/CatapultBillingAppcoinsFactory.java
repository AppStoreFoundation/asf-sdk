package com.appcoins.sdk.android_appcoins_billing;

import android.content.Context;

public class CatapultBillingAppcoinsFactory {

    public static CatapultAppcoinsBilling BuildAppcoinsBilling(Context ctx, String base64PublicKey){
        IabHelper iabHelper = new IabHelper(ctx,base64PublicKey);
        return new CatapultAppcoinsBilling(iabHelper);
    }

}

