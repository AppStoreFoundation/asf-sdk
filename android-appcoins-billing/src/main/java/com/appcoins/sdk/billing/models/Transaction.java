package com.appcoins.sdk.billing.models;

public class Transaction {

  private final String uid;
  private final String hash;
  private final String orderReference;
  private final String status;

  public Transaction(String uid, String hash, String orderReference, String status) {

    this.uid = uid;
    this.hash = hash;
    this.orderReference = orderReference;
    this.status = status;
  }

  public Transaction() {
    this.uid = "";
    this.hash = "";
    this.orderReference = "";
    this.status = "";
  }

  public String getUid() {
    return uid;
  }

  public String getHash() {
    return hash;
  }

  public String getOrderReference() {
    return orderReference;
  }

  public String getStatus() {
    return status;
  }

  public enum Status {
    PENDING, PENDING_SERVICE_AUTHORIZATION, SETTLED, PROCESSING, COMPLETED, PENDING_USER_PAYMENT,
    INVALID_TRANSACTION, FAILED, CANCELED
  }
}
