package com.appcoins.sdk.billing.models.billing;

public class TransactionResponse {

  private final String uid;
  private final String hash;
  private final String orderReference;
  private final String status;
  private boolean error;
  private int responseCode;

  public TransactionResponse(String uid, String hash, String orderReference, String status,
      boolean error, int responseCode) {

    this.uid = uid;
    this.hash = hash;
    this.orderReference = orderReference;
    this.status = status;
    this.error = error;
    this.responseCode = responseCode;
  }

  public TransactionResponse(int responseCode) {
    this.uid = "";
    this.hash = "";
    this.orderReference = "";
    this.status = "";
    this.error = true;
    this.responseCode = responseCode;
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

  public boolean hasError() {
    return error;
  }

  public int getResponseCode() {
    return responseCode;
  }
}
