package com.appcoins.sdk.billing.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import com.appcoins.sdk.billing.layouts.CardNumberEditText;
import com.appcoins.sdk.billing.layouts.CreditCardLayout;
import com.sdk.appcoins_adyen.card.CardType;
import com.sdk.appcoins_adyen.utils.CardValidationUtils;

public class CardNumberTextWatcher implements TextWatcher {

  private static final char DIGIT_SEPARATOR = ' ';
  private CreditCardLayout creditCardLayout;
  private CardNumberEditText editText;
  private EditText nextViewToFocus;
  private EditText cvvEditText;
  private boolean ignore;

  public CardNumberTextWatcher(CreditCardLayout creditCardLayout, CardNumberEditText editText,
      EditText nextViewToFocus, EditText cvvEditText) {
    this.creditCardLayout = creditCardLayout;
    this.editText = editText;
    this.nextViewToFocus = nextViewToFocus;
    this.cvvEditText = cvvEditText;
  }

  @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if (CardValidationUtils.isShortenCardNumber(charSequence.toString())) {
      ignore = true;
    }
  }

  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if (ignore) {
      ignore = false;
      editText.setSelection(charSequence.length());
    } else {
      if (charSequence.length() > 0) {
        String rawCardNumber = CardValidationUtils.getCardNumberRawValue(charSequence.toString());
        if (isValidCardNumber(rawCardNumber)) {
          creditCardLayout.setCardNumberValid(true);
          changeFocusOfInput(rawCardNumber);
        } else {
          if (CardValidationUtils.isShortenCardNumber(charSequence.toString()) && isValidCardNumber(
              CardValidationUtils.getCardNumberRawValue(editText.getCacheSavedNumber()))) {
            creditCardLayout.setCardNumberValid(true);
          } else {
            creditCardLayout.setCardNumberValid(false);
          }
        }
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
    Editable text = editText.getText();
    editText.setCacheSavedNumber(text.toString());
    editText.setText(String.format("••••%s", text.subSequence(text.length() - 4, text.length())));
    editText.setSelection(editText.getText()
        .length());
    nextViewToFocus.setVisibility(View.VISIBLE);
    cvvEditText.setVisibility(View.VISIBLE);
    nextViewToFocus.requestFocus();
  }

  private boolean isValidCardNumber(String cardNumber) {
    return CardValidationUtils.isValidCardNumber(
        CardValidationUtils.getCardNumberRawValue(cardNumber));
  }
}
