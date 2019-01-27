package com.appcoins.sdk.android_appcoins_billing;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.util.Log;
import java.util.List;

public class RepositoryServiceConnection implements ServiceConnection, RepositoryConnection {
  private final Context context;
  private final ConnectionLifeCycle connectionLifeCycle;

  public RepositoryServiceConnection(Context context, ConnectionLifeCycle connectionLifeCycle) {
    this.context = context;
    this.connectionLifeCycle = connectionLifeCycle;
  }

  private static final String TAG = RepositoryServiceConnection.class.getSimpleName();

  @Override public void onServiceConnected(ComponentName name, IBinder service) {
    Log.d(TAG,
        "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
    connectionLifeCycle.onConnect(service);
  }

  @Override public void onServiceDisconnected(ComponentName name) {
    Log.d(TAG, "onServiceDisconnected() called with: name = [" + name + "]");
  }

  @Override public void startService() {
    Intent serviceIntent = new Intent(BuildConfig.IAB_BIND_ACTION);
    serviceIntent.setPackage(BuildConfig.IAB_BIND_PACKAGE);

    List<ResolveInfo> intentServices = context.getPackageManager()
        .queryIntentServices(serviceIntent, 0);
    if (intentServices != null && !intentServices.isEmpty()) {
      context.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
    }
  }

  @Override public void stopService() {
    context.unbindService(this);
  }
}
