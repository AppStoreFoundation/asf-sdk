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

  private PaymentMethodsModel() {
    this.paymentMethods = new ArrayList<>();
    this.hasError = true;
  }

  public static PaymentMethodsModel createErrorPaymentMethodsModel() {
    return new PaymentMethodsModel();
  }

  public List<PaymentMethod> getPaymentMethods() {
    return paymentMethods;
  }

  public boolean hasError() {
    return hasError;
  }
}
