package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.content.Context;

import com.appcoins.sdk.android_appcoins_billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.android_appcoins_billing.helpers.IabHelper;

public class CatapultBillingAppcoinsFactory {

    public static CatapultAppcoinsBilling BuildAppcoinsBilling(Context ctx, String base64PublicKey){
        IabHelper iabHelper = new IabHelper(ctx,base64PublicKey);
        return new CatapultAppcoinsBilling(iabHelper);
    }

}

