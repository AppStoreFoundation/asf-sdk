package com.asf.appcoins.sdk.iab.payment;

import com.asf.appcoins.sdk.core.transaction.Transaction;

/**
 * Created by neuro on 08-03-2018.
 */
public final class PaymentDetails {

  private final String skuId;
  private PaymentStatus paymentStatus;
  private Transaction transaction;

  PaymentDetails(PaymentStatus paymentStatus, String skuId) {
    this.paymentStatus = paymentStatus;
    this.skuId = skuId;
  }

  PaymentDetails(PaymentStatus paymentStatus, String skuId, Transaction transaction) {
    this.paymentStatus = paymentStatus;
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

  public PaymentStatus getPaymentStatus() {
    return paymentStatus;
  }

  void setPaymentStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }
}
