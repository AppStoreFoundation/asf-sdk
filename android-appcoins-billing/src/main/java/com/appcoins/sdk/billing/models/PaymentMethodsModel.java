package com.appcoins.sdk.billing.models;

import java.math.BigDecimal;

public class PaymentMethodsModel {

  private final BigDecimal value;
  private final String currency;
  private final String paymentMethod;
  private final StoredMethodDetails storedMethodDetails;
  private final boolean error;

  public PaymentMethodsModel(BigDecimal value, String currency, String paymentMethod,
      StoredMethodDetails storedMethodDetails, boolean error) {
    this.value = value;
    this.currency = currency;
    this.paymentMethod = paymentMethod;
    this.storedMethodDetails = storedMethodDetails;
    this.error = error;
  }

  public PaymentMethodsModel() {
    this.value = null;
    this.currency = "";
    this.paymentMethod = "";
    this.storedMethodDetails = null;
    this.error = true;
  }

  public BigDecimal getValue() {
    return value;
  }

  public String getCurrency() {
    return currency;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public boolean hasError() {
    return error;
  }

  public StoredMethodDetails getStoredMethodDetails() {
    return storedMethodDetails;
  }
}
