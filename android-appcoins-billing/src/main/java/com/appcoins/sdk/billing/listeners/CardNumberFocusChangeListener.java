package com.appcoins.sdk.billing.listeners;

import android.view.View;
import com.appcoins.sdk.billing.layouts.CardNumberEditText;

public class CardNumberFocusChangeListener implements View.OnFocusChangeListener {

  private CardNumberEditText cardNumber;

  public CardNumberFocusChangeListener(CardNumberEditText cardNumber) {

    this.cardNumber = cardNumber;
  }

  @Override public void onFocusChange(View view, boolean hasFocus) {
    if (hasFocus) {
      if (cardNumber.getText()
          .toString()
          .contains("•")) {
        cardNumber.setText(cardNumber.getCacheSavedNumber());
      }
    }
  }
}
