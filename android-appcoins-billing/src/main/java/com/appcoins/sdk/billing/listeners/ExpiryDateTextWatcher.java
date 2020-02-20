package com.appcoins.sdk.billing.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class ExpiryDateTextWatcher implements TextWatcher {

  public static final String SEPARATOR = "/";
  private static final String DATE_FORMAT = "MM" + SEPARATOR + "yy";

  private static final int MAX_SECOND_DIGIT_MONTH = 1;
  private EditText editText;

  public ExpiryDateTextWatcher(EditText editText) {

    this.editText = editText;
  }

  @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
}
