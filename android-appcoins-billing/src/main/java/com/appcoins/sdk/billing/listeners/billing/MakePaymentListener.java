package com.appcoins.sdk.billing.listeners.billing;

import com.appcoins.sdk.billing.models.billing.AdyenTransactionModel;

public interface MakePaymentListener {

  void onResponse(AdyenTransactionModel adyenTransactionModel);
}
