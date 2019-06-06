package com.asf.appcoins.sdk.ads.repository;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import com.asf.appcoins.sdk.ads.BuildConfig;
import java.util.List;

public class AppcoinsAdvertisementConnection implements ServiceConnection {

  private final ConnectionLifeCycle connectionLifeCycle;
  private Context context;
  private AppcoinsAdvertisementListenner listenner;

  public AppcoinsAdvertisementConnection(Context context,ConnectionLifeCycle connectionLifeCycle){
    this.context = context;
    this.connectionLifeCycle = connectionLifeCycle;
  }


  @Override public void onServiceConnected(ComponentName name, IBinder service) {
    connectionLifeCycle.onConnect(service,listenner);
  }

  @Override public void onServiceDisconnected(ComponentName name) {

  }

  public void startConnection(final AppcoinsAdvertisementListenner appcoinsAdvertisementListenner) {
    this.listenner = appcoinsAdvertisementListenner;
    Intent serviceIntent = new Intent(BuildConfig.IAB_BIND_ACTION);

    serviceIntent.setPackage(BuildConfig.IAB_BIND_PACKAGE);

    List<ResolveInfo> intentServices = context.getPackageManager()
        .queryIntentServices(serviceIntent, 0);
    if (intentServices != null && !intentServices.isEmpty()) {
      context.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
    }
  }
}
