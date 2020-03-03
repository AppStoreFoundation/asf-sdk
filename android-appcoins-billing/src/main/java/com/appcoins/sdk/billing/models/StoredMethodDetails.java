package com.appcoins.sdk.billing.models;

public class StoredMethodDetails {

  private final String cardNumber;
  private final int expiryMonth;
  private final int expiryYear;
  private final String paymentId;
  private String type;

  public StoredMethodDetails(String cardNumber, int expiryMonth, int expiryYear, String paymentId,
      String type) {

    this.cardNumber = cardNumber;
    this.expiryMonth = expiryMonth;
    this.expiryYear = expiryYear;
    this.paymentId = paymentId;
    this.type = type;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public int getExpiryMonth() {
    return expiryMonth;
  }

  public int getExpiryYear() {
    return expiryYear;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public String getType() {
    return type;
  }
}
