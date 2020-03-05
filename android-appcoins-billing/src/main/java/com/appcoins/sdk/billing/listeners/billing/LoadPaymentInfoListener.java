package com.appcoins.sdk.billing.listeners.billing;

import com.appcoins.sdk.billing.models.billing.AdyenPaymentMethodsModel;

public interface LoadPaymentInfoListener {

  void onResponse(AdyenPaymentMethodsModel adyenPaymentMethodsModel);
}
