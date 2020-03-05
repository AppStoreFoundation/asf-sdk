package com.appcoins.sdk.billing.listeners.payasguest;

import android.view.View;
import com.appcoins.sdk.billing.layouts.CardNumberEditText;
import com.sdk.appcoins_adyen.utils.CardValidationUtils;

public class CardNumberFocusChangeListener implements View.OnFocusChangeListener {

  private CardNumberEditText cardNumber;

  public CardNumberFocusChangeListener(CardNumberEditText cardNumber) {

    this.cardNumber = cardNumber;
  }

  @Override public void onFocusChange(View view, boolean hasFocus) {
    if (hasFocus && CardValidationUtils.isShortenCardNumber(cardNumber.getText()
        .toString())) {
      cardNumber.setText(cardNumber.getCacheSavedNumber());
    }
  }
}
