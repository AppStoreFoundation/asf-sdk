package com.appcoins.sdk.billing.listeners;

import com.appcoins.sdk.billing.models.AdyenTransactionModel;

public interface MakePaymentListener {

  void onResponse(AdyenTransactionModel adyenTransactionModel);
}
