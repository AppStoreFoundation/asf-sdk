package com.appcoins.sdk.billing;

import android.os.IBinder;

public interface ConnectionLifeCycle {
  void onConnect(IBinder service, final AppCoinsBillingStateListener listener);

  void onDisconnect(final AppCoinsBillingStateListener listener);

  void onWalletNotInstalled(final AppCoinsBillingStateListener listener);
}
