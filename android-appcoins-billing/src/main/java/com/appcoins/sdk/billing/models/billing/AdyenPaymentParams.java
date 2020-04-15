package com.appcoins.sdk.billing.models.billing;

public class AdyenPaymentParams {

  private final String cardPaymentMethod;
  private final boolean shouldStorePaymentMethod;
  private final String returnUrl;

  public AdyenPaymentParams(String cardPaymentMethod, boolean shouldStorePaymentMethod,
      String returnUrl) {

    this.cardPaymentMethod = cardPaymentMethod;
    this.shouldStorePaymentMethod = shouldStorePaymentMethod;
    this.returnUrl = returnUrl;
  }

  public String getCardPaymentMethod() {
    return cardPaymentMethod;
  }

  public boolean shouldStorePaymentMethod() {
    return shouldStorePaymentMethod;
  }

  public String getReturnUrl() {
    return returnUrl;
  }
}
