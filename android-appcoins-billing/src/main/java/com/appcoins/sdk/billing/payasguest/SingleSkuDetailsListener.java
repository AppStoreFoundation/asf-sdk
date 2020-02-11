package com.appcoins.sdk.billing.payasguest;

import com.appcoins.sdk.billing.SkuDetails;

interface SingleSkuDetailsListener {

  void onResponse(boolean error, SkuDetails skuDetails);
}
