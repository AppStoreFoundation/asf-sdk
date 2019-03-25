package com.appcoins.sdk.android_appcoins_billing;

import android.os.IBinder;
import com.appcoins.sdk.billing.AppCoinsBillingStateListener;

public interface ConnectionLifeCycle {
  void onConnect(IBinder service, final AppCoinsBillingStateListener listener);

  void onDisconnect(final AppCoinsBillingStateListener listener);
}
