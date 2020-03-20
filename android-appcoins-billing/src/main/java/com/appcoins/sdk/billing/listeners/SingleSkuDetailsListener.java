package com.appcoins.sdk.billing.listeners;

import com.appcoins.sdk.billing.SkuDetails;

public interface SingleSkuDetailsListener {

  void onResponse(boolean error, SkuDetails skuDetails);
}
