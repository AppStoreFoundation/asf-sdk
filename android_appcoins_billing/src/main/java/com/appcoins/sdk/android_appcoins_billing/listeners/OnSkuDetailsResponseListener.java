package com.appcoins.sdk.android_appcoins_billing.listeners;

import com.appcoins.sdk.billing.ResponseListener;
import com.appcoins.sdk.billing.SkuDetails;

import java.util.List;

public interface OnSkuDetailsResponseListener extends ResponseListener {

    void onSkuDetailsResponseListener(int code, List<SkuDetails> skuDetailsList);
}
