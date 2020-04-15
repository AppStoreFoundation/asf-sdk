package com.appcoins.sdk.billing.listeners.payasguest;

import android.view.View;
import android.widget.EditText;
import com.appcoins.sdk.billing.layouts.CardNumberEditText;
import com.sdk.appcoins_adyen.utils.CardValidationUtils;

public class CardNumberFocusChangeListener implements View.OnFocusChangeListener {

  private final EditText expiryDateEditText;
  private final EditText cvvEditText;
  private CardNumberEditText cardNumber;

  public CardNumberFocusChangeListener(CardNumberEditText cardNumber, EditText expiryDateEditText,
      EditText cvvEditText) {

    this.cardNumber = cardNumber;
    this.expiryDateEditText = expiryDateEditText;
    this.cvvEditText = cvvEditText;
  }

  @Override public void onFocusChange(View view, boolean hasFocus) {
    if (hasFocus && CardValidationUtils.isShortenCardNumber(cardNumber.getText()
        .toString())) {
      cardNumber.setText(cardNumber.getCacheSavedNumber());
      cardNumber.setEnabled(true);
      expiryDateEditText.setVisibility(View.INVISIBLE);
      cvvEditText.setVisibility(View.INVISIBLE);
    }
  }
}
