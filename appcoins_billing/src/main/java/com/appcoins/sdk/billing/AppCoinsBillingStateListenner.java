package com.appcoins.sdk.billing;

public interface AppCoinsBillingStateListenner {
  void onBillingSetupFinished(int responseCode);
}
