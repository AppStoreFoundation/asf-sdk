package com.appcoins.sdk.billing.listeners;

import com.appcoins.sdk.billing.models.TransactionResponse;

public interface GetTransactionListener {

  void onResponse(TransactionResponse transactionResponse);
}
