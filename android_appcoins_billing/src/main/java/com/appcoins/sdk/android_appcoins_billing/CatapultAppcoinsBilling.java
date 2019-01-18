package com.appcoins.sdk.android_appcoins_billing;

import android.app.Activity;
import android.util.Log;

import com.appcoins.sdk.billing.AppcoinsBilling;
import com.appcoins.sdk.billing.ResponseListener;
import com.appcoins.sdk.billing.SkuDetailsParam;

import java.util.List;

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

    @Override
    public void launchPurchaseFlow(Object act, String sku, String itemType, List<String> oldSkus, int requestCode, ResponseListener listener, String extraData) {
        try {
            iabHelper.launchPurchaseFlow((Activity) act,sku,itemType,oldSkus,requestCode,(OnIabPurchaseFinishedListener)listener,extraData);
        } catch (IabAsyncInProgressException e) {

        }
    }


    public void startService(final OnIabSetupFinishedListener listener){
        iabHelper.startService(listener);
    }
}
