package com.appcoins.sdk.billing.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CardNumberTextWatcher implements TextWatcher {

  private static final char DIGIT_SEPARATOR = ' ';
  private EditText editText;

  public CardNumberTextWatcher(EditText editText) {
    this.editText = editText;
  }

  @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
  }

  @Override public void afterTextChanged(Editable editable) {
    final String initial = editable.toString();
    String processed = initial.trim();
    processed = processed.replaceAll("(\\d{4})(?=\\d)", "$1" + DIGIT_SEPARATOR);
    if (!initial.equals(processed)) {
      editText.setText(processed);
      editText.setSelection(processed.length());
    }
  }
}
