package com.appcoins.sdk.billing.service.adyen;

public enum AdyenPaymentMethod {

  CREDIT_CARD("scheme", "credit_card"), PAYPAL("paypal", "paypal");

  private final String adyenType;
  private final String transactionType;

  AdyenPaymentMethod(String adyenType, String transactionType) {
    this.adyenType = adyenType;
    this.transactionType = transactionType;
  }

  public String getAdyenType() {
    return adyenType;
  }

  public String getTransactionType() {
    return transactionType;
  }
}
