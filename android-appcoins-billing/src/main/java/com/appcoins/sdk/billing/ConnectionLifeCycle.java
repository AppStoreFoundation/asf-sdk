package com.appcoins.sdk.billing;

import android.os.IBinder;
import com.appcoins.sdk.billing.listeners.AppCoinsBillingStateListener;

public interface ConnectionLifeCycle {
  void onConnect(IBinder service, final AppCoinsBillingStateListener listener);

  void onDisconnect(final AppCoinsBillingStateListener listener);
}
