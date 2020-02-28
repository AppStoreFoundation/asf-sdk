package com.appcoins.sdk.billing.listeners;

import android.view.View;
import android.widget.EditText;

public class CardNumberFocusChangeListener implements View.OnFocusChangeListener {

  private String cacheCardNumber = "";
  private EditText cardNumber;

  public CardNumberFocusChangeListener(EditText cardNumber) {

    this.cardNumber = cardNumber;
  }

  @Override public void onFocusChange(View view, boolean hasFocus) {
    if (hasFocus) {
      if (cardNumber.getText()
          .toString()
          .contains("•")) {
        cardNumber.setText(cacheCardNumber);
      }
    }
  }

  void setCacheCardNumber(String cacheCardNumber) {
    this.cacheCardNumber = cacheCardNumber;
  }
}
