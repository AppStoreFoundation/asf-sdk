package com.appcoins.sdk.billing.layouts;

import android.content.Context;
import android.widget.RelativeLayout;

public class CreditCardLayout extends RelativeLayout {

  private boolean isCardNumberValid = false;
  private boolean isExpiryDateValid = false;
  private boolean isCvvValid = false;
  private String storedPaymentId = "";
  private FieldValidationListener fieldValidationListener;

  public CreditCardLayout(Context context) {
    super(context);
  }

  public boolean isCardNumberValid() {
    return isCardNumberValid;
  }

  public void setCardNumberValid(boolean cardNumberValid) {
    isCardNumberValid = cardNumberValid;
    onFieldChanged();
  }

  public void setExpiryDateValid(boolean expiryDateValid) {
    isExpiryDateValid = expiryDateValid;
    onFieldChanged();
  }

  public void setCvvValid(boolean cvvValid) {
    isCvvValid = cvvValid;
    onFieldChanged();
  }

  public String getStoredPaymentId() {
    return storedPaymentId;
  }

  public void setStoredPaymentId(String storedPaymentId) {
    this.storedPaymentId = storedPaymentId;
    onFieldChanged();
  }

  public void setFieldValidationListener(FieldValidationListener fieldValidationListener) {
    this.fieldValidationListener = fieldValidationListener;
  }

  private void onFieldChanged() {
    if (fieldValidationListener != null) {
      fieldValidationListener.onFieldChanged(isCardNumberValid, isExpiryDateValid, isCvvValid,
          storedPaymentId);
    }
  }
}
