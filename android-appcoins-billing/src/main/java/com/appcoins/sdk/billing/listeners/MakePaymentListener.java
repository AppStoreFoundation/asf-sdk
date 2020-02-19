package com.appcoins.sdk.billing.listeners;

import com.appcoins.sdk.billing.models.AdyenTransactionResponse;

public interface MakePaymentListener {

  void onResponse(AdyenTransactionResponse adyenTransactionResponse);
}
