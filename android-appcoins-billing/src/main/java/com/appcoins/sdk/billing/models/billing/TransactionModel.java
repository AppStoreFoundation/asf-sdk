package com.appcoins.sdk.billing.models.billing;

import com.appcoins.sdk.billing.models.Transaction;

public class TransactionModel {

  private final Transaction transaction;
  private boolean error;
  private int responseCode;

  public TransactionModel(Transaction transaction, boolean error, int responseCode) {

    this.transaction = transaction;
    this.error = error;
    this.responseCode = responseCode;
  }

  public TransactionModel(int responseCode) {
    this.transaction = Transaction.createErrorTransaction();
    this.error = true;
    this.responseCode = responseCode;
  }

  public static TransactionModel createErrorTransactionModel(int responseCode) {
    return new TransactionModel(responseCode);
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public String getUid() {
    return transaction.getUid();
  }

  public String getHash() {
    return transaction.getHash();
  }

  public String getOrderReference() {
    return transaction.getOrderReference();
  }

  public Transaction.Status getStatus() {
    return transaction.getStatus();
  }

  public String getGateway() {
    return transaction.getGateway();
  }

  public boolean hasError() {
    return error;
  }

  public int getResponseCode() {
    return responseCode;
  }
}
