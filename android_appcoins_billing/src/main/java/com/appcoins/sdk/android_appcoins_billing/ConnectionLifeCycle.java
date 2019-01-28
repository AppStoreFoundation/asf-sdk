package com.appcoins.sdk.android_appcoins_billing;

import android.os.IBinder;
import com.appcoins.sdk.billing.AppCoinsBillingStateListenner;

public interface ConnectionLifeCycle {
  void onConnect(IBinder service, final AppCoinsBillingStateListenner listener);

  void onDisconnect(final AppCoinsBillingStateListenner listener);
}
