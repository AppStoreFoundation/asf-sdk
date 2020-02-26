package com.appcoins.sdk.billing.listeners;

import com.appcoins.sdk.billing.models.PaymentMethodsModel;

public interface LoadPaymentInfoListener {

  void onResponse(PaymentMethodsModel paymentMethodsModel);
}
