package com.appcoins.sdk.billing.listeners.payasguest;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.appcoins.sdk.billing.layouts.CreditCardLayout;
import com.sdk.appcoins_adyen.utils.CardValidationUtils;

public class CvvTextWatcher implements TextWatcher {

  private CreditCardLayout creditCardLayout;
  private EditText cvvEditText;
  private EditText previousViewToFocus;
  private String beforeTextChanged = "";

  public CvvTextWatcher(CreditCardLayout creditCardLayout, EditText cvvEditText,
      EditText previousViewToFocus) {
    this.creditCardLayout = creditCardLayout;
    this.cvvEditText = cvvEditText;
    this.previousViewToFocus = previousViewToFocus;
  }

  @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    beforeTextChanged = charSequence.toString();
  }

  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if (CardValidationUtils.isValidSecurityCode(charSequence.toString())) {
      creditCardLayout.setCvvValid(true);
      cvvEditText.setTextColor(Color.parseColor("#292929"));
    } else {
      creditCardLayout.setCvvValid(false);
      if (charSequence.length() == 0 && beforeTextChanged.length() > 0) {
        goToPreviousInputFocus();
      } else if (charSequence.length() == CardValidationUtils.CVV_MAX_LENGTH
          || charSequence.length() == CardValidationUtils.CVV_MAX_LENGTH - 1) {
        cvvEditText.setTextColor(Color.RED);
      } else {
        cvvEditText.setTextColor(Color.parseColor("#292929"));
      }
    }
  }

  @Override public void afterTextChanged(Editable editable) {
  }

  private void goToPreviousInputFocus() {
    if (previousViewToFocus.isEnabled()) {
      previousViewToFocus.requestFocus();
    }
  }
}
