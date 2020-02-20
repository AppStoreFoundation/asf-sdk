package com.appcoins.sdk.billing;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.appcoins.sdk.billing.helpers.IBinderWalletNotInstalled;
import com.appcoins.sdk.billing.helpers.WalletUtils;
import com.appcoins.sdk.billing.listeners.AppCoinsBillingStateListener;

public class RepositoryServiceConnection implements ServiceConnection, RepositoryConnection {
  private static final String TAG = RepositoryServiceConnection.class.getSimpleName();
  private final Context context;
  private final ConnectionLifeCycle connectionLifeCycle;
  private AppCoinsBillingStateListener listener;
  private boolean hasWalletInstalled;

  public RepositoryServiceConnection(Context context, ConnectionLifeCycle connectionLifeCycle) {
    this.context = context;
    this.connectionLifeCycle = connectionLifeCycle;
  }

  @Override public void onServiceConnected(ComponentName name, IBinder service) {
    Log.d(TAG,
        "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
    connectionLifeCycle.onConnect(service, listener);
  }

  @Override public void onServiceDisconnected(ComponentName name) {
    Log.d(TAG, "onServiceDisconnected() called with: name = [" + name + "]");
    connectionLifeCycle.onDisconnect(listener);
  }

  @Override public void onBindingDied(ComponentName name) {
    connectionLifeCycle.onDisconnect(listener);
  }

  @Override public void onNullBinding(ComponentName name) {
    connectionLifeCycle.onDisconnect(listener);
  }

  @Override public void startConnection(final AppCoinsBillingStateListener listener) {
    this.listener = listener;
    if (WalletUtils.hasWalletInstalled()) {
      hasWalletInstalled = true;
      String packageName = WalletUtils.getBillingServicePackageName();
      walletInstalledBehaviour(packageName);
    } else {
      hasWalletInstalled = false;
      walletNotInstalledBehaviour();
    }
  }

  @Override public void endConnection() {
    if (hasWalletInstalled) {
      context.unbindService(this);
    }
    connectionLifeCycle.onDisconnect(listener);
  }

  private void walletNotInstalledBehaviour() {
    onServiceConnected(new ComponentName("", ""), new IBinderWalletNotInstalled());
  }

  private void walletInstalledBehaviour(String packageName) {
    String iabAction = WalletUtils.getIabAction();
    Intent serviceIntent = new Intent(iabAction);
    serviceIntent.setPackage(packageName);
    context.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
  }
}
