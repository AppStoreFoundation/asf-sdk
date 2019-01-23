package com.appcoins.sdk.android_appcoins_billing.listeners;

import com.appcoins.sdk.android_appcoins_billing.types.IabResult;
import com.appcoins.sdk.billing.Purchase;
import com.appcoins.sdk.billing.ResponseListener;

public interface OnIabPurchaseFinishedListener extends ResponseListener {

    void onIabPurchaseFinished(IabResult result, Purchase info);

}
