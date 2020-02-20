package com.appcoins.sdk.billing;

import com.appcoins.sdk.billing.listeners.AppCoinsBillingStateListener;

public interface RepositoryConnection {
  void startConnection(final AppCoinsBillingStateListener listener);

  void endConnection();
}
