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
    if (intent != null) {
      if (intent.hasExtra(PARAM_WALLET_PACKAGE_NAME)) {
        Log.d(TAG, "Received wallet package name: " + intent.getStringExtra(PARAM_WALLET_PACKAGE_NAME));
        // intent received that contains the wallet that answered to our broadcast
        // TODO Add logic to handle possible multiple intents received.
        // create a preference accessor. This is for global app preferences.
        final AppPreferences appPreferences = new AppPreferences(
            getApplicationContext());
        String packageName = intent.getStringExtra(PARAM_WALLET_PACKAGE_NAME);
        appPreferences.put(PREFERENCE_WALLET_PCKG_NAME, packageName);

      }
    }
    stopSelf(startId);
    return super.onStartCommand(intent, flags, startId);
  }

  @Override public IBinder onBind(Intent intent) {
    return null;
  }
}
