package com.appcoins.sdk.billing.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.sdk.appcoins_adyen.utils.CardValidationUtils;

public class ExpiryDateTextWatcher implements TextWatcher {

  private static final String SEPARATOR = "/";

  private static final int MAX_SECOND_DIGIT_MONTH = 1;
  private EditText editText;
  private EditText nextViewToFocus;
  private EditText previousViewToFocus;
  private String beforeTextChanged = "";

  public ExpiryDateTextWatcher(EditText editText, EditText nextViewToFocus,
      EditText previousViewToFocus) {
    this.editText = editText;
    this.nextViewToFocus = nextViewToFocus;
    this.previousViewToFocus = previousViewToFocus;
  }

  @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    beforeTextChanged = charSequence.toString();
  }

  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if (CardValidationUtils.isValidExpiryDate(
        CardValidationUtils.getDate(charSequence.toString()))) {
      goToNextInputFocus();
    } else if (charSequence.length() == 0 && beforeTextChanged.length() > 0) {
      goToPreviousInputFocus();
    }
  }

  @Override public void afterTextChanged(Editable editable) {
    final String initial = editable.toString();
    // remove digits
    String processed = initial.replaceAll("\\D", "");
    // add separator
    processed = processed.replaceAll("(\\d{2})(?=\\d)", "$1" + SEPARATOR);
    // add tailing zero to month
    if (processed.length() == 1
        && isStringInt(processed)
        && Integer.parseInt(processed) > MAX_SECOND_DIGIT_MONTH) {
      processed = "0" + processed;
    }

    if (!initial.equals(processed)) {
      editText.setText(processed);
      editText.setSelection(processed.length());
    }
  }

  private boolean isStringInt(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException ex) {
      return false;
    }
  }

  private void goToNextInputFocus() {
    nextViewToFocus.requestFocus();
  }

  private void goToPreviousInputFocus() {
    previousViewToFocus.requestFocus();
  }
}
