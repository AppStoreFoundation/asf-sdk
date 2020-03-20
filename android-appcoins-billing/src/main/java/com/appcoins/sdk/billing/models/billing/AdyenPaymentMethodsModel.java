package com.appcoins.sdk.billing.models.billing;

import com.appcoins.sdk.billing.models.payasguest.StoredMethodDetails;
import java.math.BigDecimal;

public class AdyenPaymentMethodsModel {

  private final BigDecimal value;
  private final String currency;
  private final String paymentMethod;
  private final StoredMethodDetails storedMethodDetails;
  private final boolean error;

  public AdyenPaymentMethodsModel(BigDecimal value, String currency, String paymentMethod,
      StoredMethodDetails storedMethodDetails, boolean error) {
    this.value = value;
    this.currency = currency;
    this.paymentMethod = paymentMethod;
    this.storedMethodDetails = storedMethodDetails;
    this.error = error;
  }

  public AdyenPaymentMethodsModel() {
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
