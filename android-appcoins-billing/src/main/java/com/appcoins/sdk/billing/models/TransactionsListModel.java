package com.appcoins.sdk.billing.models;

import com.appcoins.sdk.billing.models.billing.TransactionModel;
import java.util.Collections;
import java.util.List;

public class TransactionsListModel {

  private final List<TransactionModel> transactionModelList;
  private final boolean hasError;

  public TransactionsListModel(List<TransactionModel> transactionModelList, boolean hasError) {

    this.transactionModelList = transactionModelList;
    this.hasError = hasError;
  }

  private TransactionsListModel() {
    this.transactionModelList = Collections.emptyList();
    this.hasError = true;
  }

  public static TransactionsListModel createErrorTransactionListModel() {
    return new TransactionsListModel();
  }

  public List<TransactionModel> getTransactionModelList() {
    return transactionModelList;
  }

  public boolean hasError() {
    return hasError;
  }
}
