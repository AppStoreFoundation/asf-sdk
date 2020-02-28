package com.appcoins.sdk.billing.models;

public class Transaction {

  public Transaction(String uid, String hash, String orderReference, Status status) {

  }

  public enum Status {
    PENDING, PENDING_SERVICE_AUTHORIZATION, SETTLED, PROCESSING, COMPLETED, PENDING_USER_PAYMENT,
    INVALID_TRANSACTION, FAILED, CANCELED
  }
}
