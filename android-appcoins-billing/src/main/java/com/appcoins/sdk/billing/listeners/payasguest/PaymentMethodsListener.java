package com.appcoins.sdk.billing.listeners.payasguest;

import com.appcoins.sdk.billing.models.payasguest.PaymentMethodsModel;

public interface PaymentMethodsListener {

  void onResponse(PaymentMethodsModel paymentMethodsModel);
}
