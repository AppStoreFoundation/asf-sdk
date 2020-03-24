package com.appcoins.sdk.billing.layouts;

import android.content.Context;
import android.widget.EditText;

public class CardNumberEditText extends EditText {

  private String cacheSavedNumber = "";

  public CardNumberEditText(Context context) {
    super(context);
  }

  public String getCacheSavedNumber() {
    return cacheSavedNumber;
  }

  public void setCacheSavedNumber(String cacheSavedNumber) {
    this.cacheSavedNumber = cacheSavedNumber;
  }
}
