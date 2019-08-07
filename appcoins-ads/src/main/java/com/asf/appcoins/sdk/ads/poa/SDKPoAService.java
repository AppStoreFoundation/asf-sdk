package com.asf.appcoins.sdk.ads.poa;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import net.grandcentrix.tray.AppPreferences;

import static com.asf.appcoins.sdk.ads.poa.PoAServiceConnector.PARAM_WALLET_PACKAGE_NAME;
import static com.asf.appcoins.sdk.ads.poa.PoAServiceConnector.PREFERENCE_WALLET_PCKG_NAME;

/**
 * Created by Joao Raimundo on 29/03/2018.
 */

public class SDKPoAService extends Service {
  private static final String TAG = SDKPoAService.class.getSimpleName();

  @Override public void onCreate() {
    super.onCreate();
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  @Override public IBinder onBind(Intent intent) {
    return null;
  }
}
