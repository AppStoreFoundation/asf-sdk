package com.appcoins.sdk.billing.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.sdk.appcoins_adyen.card.CardType;
import com.sdk.appcoins_adyen.utils.CardValidationUtils;

public class CardNumberTextWatcher implements TextWatcher {

  private static final char DIGIT_SEPARATOR = ' ';
  private EditText editText;
  private EditText nextViewToFocus;

  public CardNumberTextWatcher(EditText editText, EditText nextViewToFocus) {
    this.editText = editText;
    this.nextViewToFocus = nextViewToFocus;
  }

  @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
  }

  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if (charSequence.length() > 0) {
      String rawCardNumber = CardValidationUtils.getCardNumberRawValue(charSequence.toString());
      if (CardValidationUtils.isValidCardNumber(
          CardValidationUtils.getCardNumberRawValue(rawCardNumber))) {
        changeFocusOfInput(rawCardNumber);
      }
    }
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

  private void changeFocusOfInput(String numberValue) {
    final int length = numberValue.length();

    if (length == CardValidationUtils.GENERAL_CARD_NUMBER_LENGTH
        || length == CardValidationUtils.AMEX_CARD_NUMBER_LENGTH && CardType.estimate(numberValue)
        .contains(CardType.AMERICAN_EXPRESS)) {
      goToNextInputFocus();
    }
  }

  private void goToNextInputFocus() {
    nextViewToFocus.requestFocus();
  }
}
