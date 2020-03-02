package com.appcoins.sdk.billing.payasguest;

import android.os.Bundle;
import java.math.BigDecimal;

interface AdyenPaymentView {
  void close();

  void showError();

  void showLoading();

  void updateFiatPrice(BigDecimal value, String currency);

  void showCreditCardView();

  void lockRotation();

  void unlockRotation();

  void navigateToUri(String url, ActivityResultListener activityResultListener);

  void finish(Bundle bundle);

  void navigateToPaymentSelection();
}
