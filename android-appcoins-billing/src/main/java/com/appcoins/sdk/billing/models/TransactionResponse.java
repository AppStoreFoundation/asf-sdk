package com.appcoins.sdk.billing.models;

public class TransactionResponse {

  private final String uid;
  private final String hash;
  private final String orderReference;
  private final String status;
  private boolean error;

  public TransactionResponse(String uid, String hash, String orderReference, String status,
      boolean error) {

    this.uid = uid;
    this.hash = hash;
    this.orderReference = orderReference;
    this.status = status;
    this.error = error;
  }

  public TransactionResponse() {
    this.uid = "";
    this.hash = "";
    this.orderReference = "";
    this.status = "";
    this.error = true;
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
}
