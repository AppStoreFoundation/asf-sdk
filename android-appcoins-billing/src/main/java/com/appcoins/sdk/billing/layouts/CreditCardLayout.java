package com.appcoins.sdk.billing.layouts;

import android.content.Context;
import android.widget.LinearLayout;

public class CreditCardLayout extends LinearLayout {

  private boolean isCardNumberValid = false;
  private boolean isExpiryDateValid = false;
  private boolean isCvvValid = false;
  private FieldValidationListener fieldValidationListener;

  public CreditCardLayout(Context context) {
    super(context);
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

  public void setFieldValidationListener(FieldValidationListener fieldValidationListener) {
    this.fieldValidationListener = fieldValidationListener;
  }

  private void onFieldChanged() {
    if (fieldValidationListener != null) {
      fieldValidationListener.onFieldChanged(isCardNumberValid, isExpiryDateValid, isCvvValid);
    }
  }
}
