package com.appcoins.sdk.billing.listeners.billing;

import com.appcoins.sdk.billing.models.billing.PurchaseModel;

public interface PurchaseListener {

  void onResponse(PurchaseModel purchaseModel);
}
