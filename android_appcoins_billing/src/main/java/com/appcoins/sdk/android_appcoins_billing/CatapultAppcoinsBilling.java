package com.appcoins.sdk.android_appcoins_billing;

import android.util.Log;

import com.appcoins.sdk.billing.AppcoinsBilling;
import com.appcoins.sdk.billing.ResponseListener;
import com.appcoins.sdk.billing.SkuDetailsParam;

public class CatapultAppcoinsBilling implements AppcoinsBilling {

    private IabHelper iabHelper;

    public CatapultAppcoinsBilling(IabHelper iabHelper){
        this.iabHelper = iabHelper;
    }

    @Override
    public void queryPurchases(String skuType) {

    }


    @Override
    public void querySkuDetailsAsync(SkuDetailsParam skuDetailsParam , ResponseListener onSkuDetailsResponseListener)  {
        try {
            iabHelper.querySkuDetailsAsync(skuDetailsParam,(OnSkuDetailsResponseListener) onSkuDetailsResponseListener);
        } catch (IabAsyncInProgressException e) {
            Log.e("Message: ","Error querying inventory. Another async operation in progress.");
        }
    }

    public void startService(final OnIabSetupFinishedListener listener){
        iabHelper.startService(listener);
    }
}
