package com.appcoins.sdk.billing.payasguest;

import java.util.ArrayList;
import java.util.List;

class PaymentMethodsModel {

  private final List<PaymentMethod> paymentMethods;
  private final boolean hasError;

  PaymentMethodsModel(List<PaymentMethod> paymentMethods, boolean hasError) {

    this.paymentMethods = paymentMethods;
    this.hasError = hasError;
  }

  PaymentMethodsModel() {
    this.paymentMethods = new ArrayList<>();
    this.hasError = true;
  }

  List<PaymentMethod> getPaymentMethods() {
    return paymentMethods;
  }

  boolean hasError() {
    return hasError;
  }
}
