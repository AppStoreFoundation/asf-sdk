package com.appcoins.sdk.billing.layouts;

public interface FieldValidationListener {

  void onFieldChanged(boolean isCardNumberValid, boolean isExpiryDateValid, boolean isCvvValid,
      String paymentId);
}
