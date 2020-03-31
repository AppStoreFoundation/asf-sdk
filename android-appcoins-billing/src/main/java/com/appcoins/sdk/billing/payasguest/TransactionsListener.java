package com.appcoins.sdk.billing.payasguest;

import com.appcoins.sdk.billing.models.TransactionsListModel;

public interface TransactionsListener {

  void onResponse(TransactionsListModel transactionsListModel);
}
