package com.asf.appcoins.sdk.iab.entity;

/**
 * Created by neuro on 04-03-2018.
 */
public class PurchaseResult {

  private final Status status;
  private final String txHash;
  private final int resultCode;

  public PurchaseResult(Status status, String txHash, int resultCode) {
    this.status = status;
    this.txHash = txHash;
    this.resultCode = resultCode;
  }

  public PurchaseResult(Status status, int resultCode) {
    this.status = status;
    this.txHash = null;
    this.resultCode = resultCode;
  }

  public Status getStatus() {
    return status;
  }

  public String getTxHash() {
    return txHash;
  }

  public enum Status {
    SUCCESS, FAIL,
  }
}
