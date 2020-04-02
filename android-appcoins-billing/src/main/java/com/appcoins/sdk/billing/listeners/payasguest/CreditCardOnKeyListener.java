package com.appcoins.sdk.billing.listeners.payasguest;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class CreditCardOnKeyListener implements View.OnKeyListener {

  private final EditText previousViewToFocus;
  private final EditText currentView;

  public CreditCardOnKeyListener(EditText previousViewToFocus, EditText currentView) {

    this.previousViewToFocus = previousViewToFocus;
    this.currentView = currentView;
  }

  @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
    if (keyboardBackPressed(keyCode, event.getAction()) && isEmpty(currentView.getText())) {
      previousViewToFocus.requestFocus();
    }
    return false;
  }

  private boolean isEmpty(Editable text) {
    return text.toString()
        .isEmpty();
  }

  private boolean keyboardBackPressed(int keyCode, int action) {
    return keyCode == KeyEvent.KEYCODE_DEL && action == KeyEvent.ACTION_UP;
  }
}
