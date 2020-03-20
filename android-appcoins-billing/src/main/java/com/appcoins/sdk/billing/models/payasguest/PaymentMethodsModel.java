package com.appcoins.sdk.billing.models.payasguest;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodsModel {

  private final List<PaymentMethod> paymentMethods;
  private final boolean hasError;

  public PaymentMethodsModel(List<PaymentMethod> paymentMethods, boolean hasError) {

    this.paymentMethods = paymentMethods;
    this.hasError = hasError;
  }

  public PaymentMethodsModel() {
    this.paymentMethods = new ArrayList<>();
    this.hasError = true;
  }

  public List<PaymentMethod> getPaymentMethods() {
    return paymentMethods;
  }

  public boolean hasError() {
    return hasError;
  }
}
