package com.appcoins.sdk.billing.listeners;

public interface ConsumeResponseListener {

  void onConsumeResponse(int responseCode, String purchaseToken);
}
