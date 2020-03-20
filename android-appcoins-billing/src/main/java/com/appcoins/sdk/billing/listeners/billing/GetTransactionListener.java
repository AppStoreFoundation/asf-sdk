package com.appcoins.sdk.billing.listeners.billing;

import com.appcoins.sdk.billing.models.billing.TransactionResponse;

public interface GetTransactionListener {

  void onResponse(TransactionResponse transactionResponse);
}
