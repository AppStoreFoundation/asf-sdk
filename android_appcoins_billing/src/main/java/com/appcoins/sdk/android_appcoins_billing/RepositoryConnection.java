package com.appcoins.sdk.android_appcoins_billing;

import com.appcoins.sdk.billing.AppCoinsBillingStateListenner;

public interface RepositoryConnection {
  void startService(final AppCoinsBillingStateListenner listener);

  void stopService();
}
