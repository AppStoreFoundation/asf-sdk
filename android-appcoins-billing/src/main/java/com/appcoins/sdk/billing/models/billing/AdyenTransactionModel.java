package com.appcoins.sdk.billing.models.billing;

public class AdyenTransactionModel {

  private final String uid;
  private final String hash;
  private final String orderReference;
  private final String status;
  private final String pspReference;
  private final String resultCode;
  private final String url;
  private final String paymentData;
  private final String refusalReason;
  private final int refusalReasonCode;
  private final boolean error;
  private int responseCode;

  public AdyenTransactionModel(String uid, String hash, String orderReference, String status,
      String pspReference, String resultCode, String url, String paymentData, String refusalReason,
      int refusalReasonCode, boolean error, int responseCode) {

    this.uid = uid;
    this.hash = hash;
    this.orderReference = orderReference;
    this.status = status;
    this.pspReference = pspReference;
    this.resultCode = resultCode;
    this.url = url;
    this.paymentData = paymentData;
    this.refusalReason = refusalReason;
    this.refusalReasonCode = refusalReasonCode;
    this.error = error;
    this.responseCode = responseCode;
  }

  public AdyenTransactionModel(int responseCode) {
    this.uid = "";
    this.hash = "";
    this.orderReference = "";
    this.status = "FAILED";
    this.pspReference = "";
    this.resultCode = null;
    this.url = null;
    this.paymentData = null;
    this.refusalReason = null;
    this.refusalReasonCode = -1;
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

  public String getPspReference() {
    return pspReference;
  }

  public String getResultCode() {
    return resultCode;
  }

  public String getRefusalReason() {
    return refusalReason;
  }

  public int getRefusalReasonCode() {
    return refusalReasonCode;
  }

  public boolean hasError() {
    return error;
  }

  public String getUrl() {
    return url;
  }

  public String getPaymentData() {
    return paymentData;
  }

  public int getResponseCode() {
    return responseCode;
  }
}
