package com.appcoins.sdk.billing;

public interface AppCoinsBillingStateListener {
  void onBillingSetupFinished(int responseCode);
  void onBillingServiceDisconnected();
}
