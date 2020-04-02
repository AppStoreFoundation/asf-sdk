package com.appcoins.sdk.billing.listeners.billing;

import com.appcoins.sdk.billing.models.billing.TransactionModel;

public interface GetTransactionListener {

  void onResponse(TransactionModel transactionModel);
}
