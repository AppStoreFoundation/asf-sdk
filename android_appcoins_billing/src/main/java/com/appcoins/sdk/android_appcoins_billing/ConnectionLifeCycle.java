package com.appcoins.sdk.android_appcoins_billing;

import android.os.IBinder;

public interface ConnectionLifeCycle {
  void onConnect(IBinder service);
  void onDisconnect();
}
