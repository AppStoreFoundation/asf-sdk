package com.appcoins.sdk.android_appcoins_billing;

import com.appcoins.sdk.billing.AppCoinsBillingStateListener;

public interface RepositoryConnection {
  void startService(final AppCoinsBillingStateListener listener);

  void stopService();
}
