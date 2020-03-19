package com.appcoins.sdk.billing.listeners.payasguest;

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
  private boolean isShortenCardNumber;

  public CardNumberTextWatcher(CreditCardLayout creditCardLayout, CardNumberEditText editText,
      EditText nextViewToFocus, EditText cvvEditText) {
    this.creditCardLayout = creditCardLayout;
    this.editText = editText;
    this.nextViewToFocus = nextViewToFocus;
    this.cvvEditText = cvvEditText;
  }

  @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if (CardValidationUtils.isShortenCardNumber(charSequence.toString())) {
      //When the number is in the following format ••••, and the user goes back to edit it, the
      // onFocusListener is called to change the number back to the full card number and we
      // should ignore that change or an infinite cycle will occur
      isShortenCardNumber = true;
    }
  }

  @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if (isShortenCardNumber) {
      //onFocusChange changed number back to the full card number
      isShortenCardNumber = false;
      editText.setSelection(charSequence.length());
    } else {
      if (charSequence.length() > 0) {
        String rawCardNumber = CardValidationUtils.getCardNumberRawValue(charSequence.toString());
        if (isValidCardNumber(rawCardNumber)) {
          onValidCardNumber(rawCardNumber);
        } else {
          setCachedCardNumberValidity(charSequence);
        }
      }
    }
  }

  @Override public void afterTextChanged(Editable editable) {
    final String initial = editable.toString();
    if (editText.isEnabled()) {
      String processed = initial.trim();
      processed = processed.replaceAll("(\\d{4})(?=\\d)", "$1" + DIGIT_SEPARATOR);
      if (!initial.equals(processed)) {
        editText.setText(processed);
        editText.setSelection(processed.length());
      }
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
    String text = editText.getText()
        .toString()
        .trim();
    editText.setCacheSavedNumber(text);
    String format = String.format("••••%s", text.subSequence(text.length() - 4, text.length()));
    editText.setText(format);
    editText.setSelection(editText.getText()
        .length());
    nextViewToFocus.setVisibility(View.VISIBLE);
    cvvEditText.setVisibility(View.VISIBLE);
    nextViewToFocus.requestFocus();
    editText.setEnabled(false);
  }

  private boolean isValidCardNumber(String cardNumber) {
    return CardValidationUtils.isValidCardNumber(
        CardValidationUtils.getCardNumberRawValue(cardNumber));
  }

  private void setCachedCardNumberValidity(CharSequence charSequence) {
    if (CardValidationUtils.isShortenCardNumber(charSequence.toString()) && isValidCardNumber(
        CardValidationUtils.getCardNumberRawValue(editText.getCacheSavedNumber()))) {
      creditCardLayout.setCardNumberValid(true);
    } else {
      creditCardLayout.setCardNumberValid(false);
    }
  }

  private void onValidCardNumber(String rawCardNumber) {
    creditCardLayout.setCardNumberValid(true);
    changeFocusOfInput(rawCardNumber);
  }
}
