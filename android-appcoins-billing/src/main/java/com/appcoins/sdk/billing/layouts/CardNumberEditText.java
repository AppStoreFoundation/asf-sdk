package com.appcoins.sdk.billing.layouts;

import android.app.Activity;
import android.widget.EditText;

public class CardNumberEditText extends EditText {

  private String cacheSavedNumber = "";

  public CardNumberEditText(Activity activity) {
    super(activity);
  }

  public String getCacheSavedNumber() {
    return cacheSavedNumber;
  }

  public void setCacheSavedNumber(String cacheSavedNumber) {
    this.cacheSavedNumber = cacheSavedNumber;
  }
}
