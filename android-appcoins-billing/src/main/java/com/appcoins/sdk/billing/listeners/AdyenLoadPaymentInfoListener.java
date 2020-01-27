package com.appcoins.sdk.billing.listeners;

public interface AdyenLoadPaymentInfoListener {

  void onResponse(int code, String response, Exception exception);
}
