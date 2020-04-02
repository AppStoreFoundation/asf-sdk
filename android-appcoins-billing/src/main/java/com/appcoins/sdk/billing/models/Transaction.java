package com.appcoins.sdk.billing.models;

public class Transaction {

  private final String uid;
  private final String hash;
  private final String orderReference;
  private final Status status;
  private String gateway;

  public Transaction(String uid, String hash, String orderReference, Status status,
      String gateway) {

    this.uid = uid;
    this.hash = hash;
    this.orderReference = orderReference;
    this.status = status;
    this.gateway = gateway;
  }

  private Transaction() {
    this.uid = "";
    this.hash = "";
    this.orderReference = "";
    this.status = Status.INVALID_TRANSACTION;
    this.gateway = "";
  }

  public static Transaction createErrorTransaction() {
    return new Transaction();
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

  public Status getStatus() {
    return status;
  }

  public String getGateway() {
    return gateway;
  }

  public enum Status {
    PENDING, PENDING_SERVICE_AUTHORIZATION, SETTLED, PROCESSING, COMPLETED, PENDING_USER_PAYMENT,
    INVALID_TRANSACTION, FAILED, CANCELED, ERROR
  }
}
