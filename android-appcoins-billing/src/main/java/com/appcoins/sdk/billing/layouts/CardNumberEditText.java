package com.appcoins.sdk.billing.layouts;

import android.app.Activity;
import android.widget.EditText;

public class CardNumberEditText extends EditText {

  private String cacheSavedNumber = "";
  private boolean isStoredCard = false;

  public CardNumberEditText(Activity activity) {
    super(activity);
  }

  public String getCacheSavedNumber() {
    return cacheSavedNumber;
  }

  public void setCacheSavedNumber(String cacheSavedNumber) {
    this.cacheSavedNumber = cacheSavedNumber;
  }

  public boolean isStoredCard() {
    return isStoredCard;
  }

  public void setStoredCard(boolean isStoredCard) {
    this.isStoredCard = isStoredCard;
  }
}
