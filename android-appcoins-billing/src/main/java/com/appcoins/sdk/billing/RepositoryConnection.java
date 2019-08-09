package com.appcoins.sdk.billing;

public interface RepositoryConnection {
  void startConnection(final AppCoinsBillingStateListener listener);

  void endConnection();
}
