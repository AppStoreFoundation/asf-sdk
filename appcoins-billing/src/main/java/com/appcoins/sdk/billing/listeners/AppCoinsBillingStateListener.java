package com.appcoins.sdk.billing.listeners;

public interface AppCoinsBillingStateListener {
  void onBillingSetupFinished(int responseCode);
  void onBillingServiceDisconnected();
}
