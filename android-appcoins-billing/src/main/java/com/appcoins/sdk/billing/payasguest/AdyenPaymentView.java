package com.appcoins.sdk.billing.payasguest;

import android.os.Bundle;
import com.appcoins.sdk.billing.models.payasguest.StoredMethodDetails;
import java.math.BigDecimal;

interface AdyenPaymentView {
  void close(boolean withError);

  void showError();

  void showLoading();

  void updateFiatPrice(BigDecimal value, String currency);

  void showCreditCardView(StoredMethodDetails storedMethodDetails);

  void lockRotation();

  void unlockRotation();

  void navigateToUri(String url, String uid);

  void finish(Bundle bundle);

  void navigateToPaymentSelection();

  void clearCreditCardInput();

  void showCvvError();

  void showError(String errorMessage);

  void showCompletedPurchase();

  void disableBack();

  void enableBack();
}
