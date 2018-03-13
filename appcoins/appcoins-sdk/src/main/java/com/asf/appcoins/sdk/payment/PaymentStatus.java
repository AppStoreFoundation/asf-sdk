package com.asf.appcoins.sdk.payment;

import com.asf.appcoins.sdk.entity.Transaction;

/**
 * Created by neuro on 08-03-2018.
 */
public final class PaymentStatus {

  private final String skuId;
  private Transaction transaction;

  PaymentStatus(String skuId) {
    this.skuId = skuId;
  }

  PaymentStatus(String skuId, Transaction transaction) {
    this.skuId = skuId;
    this.transaction = transaction;
  }

  public String getSkuId() {
    return skuId;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }
}
