package com.appcoins.sdk.billing.listeners;

import com.appcoins.sdk.billing.models.PaymentMethodsResponse;

public interface LoadPaymentInfoListener {

  void onResponse(PaymentMethodsResponse paymentMethodsResponse);
}
