package com.appcoins.sdk.billing.payasguest;

import java.math.BigDecimal;

interface AdyenPaymentView {
  void close();

  void showError();

  void updateFiatPrice(BigDecimal value, String currency);

  void showCreditCardView();
}
