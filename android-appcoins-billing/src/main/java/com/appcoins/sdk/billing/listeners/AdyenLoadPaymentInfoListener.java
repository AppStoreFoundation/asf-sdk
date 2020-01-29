package com.appcoins.sdk.billing.listeners;

import com.adyen.checkout.base.model.PaymentMethodsApiResponse;

public interface AdyenLoadPaymentInfoListener {

  void onResponse(int code, PaymentMethodsApiResponse paymentMethodsApiResponse,
      Exception exception);
}
