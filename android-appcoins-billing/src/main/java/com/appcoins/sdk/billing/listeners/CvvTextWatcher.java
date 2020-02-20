package com.appcoins.sdk.billing.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CvvTextWatcher implements TextWatcher {

  private EditText previousViewToFocus;
  private String beforeTextChanged = "";

  public CvvTextWatcher(EditText previousViewToFocus) {
    this.previousViewToFocus = previousViewToFocus;
  }

  @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    beforeTextChanged = charSequence.toString();
  }

  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if (charSequence.length() == 0 && beforeTextChanged.length() > 0) {
      goToPreviousInputFocus();
    }
  }

  @Override public void afterTextChanged(Editable editable) {
  }

  private void goToPreviousInputFocus() {
    previousViewToFocus.requestFocus();
  }
}
