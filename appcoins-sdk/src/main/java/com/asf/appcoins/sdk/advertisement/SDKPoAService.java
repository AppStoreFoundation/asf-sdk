package com.asf.appcoins.sdk.advertisement;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import static com.asf.appcoins.sdk.advertisement.PoAServiceConnector.ACTION_BIND;
import static com.asf.appcoins.sdk.advertisement.PoAServiceConnector.MSG_SIGN_PROOF;
import static com.asf.appcoins.sdk.advertisement.PoAServiceConnector.PARAM_WALLET_PACKAGE_NAME;
import static com.asf.appcoins.sdk.advertisement.PoAServiceConnector.PREFERENCE_WALLET_PCKG_NAME;
import static com.asf.appcoins.sdk.advertisement.PoAServiceConnector.SHARED_PREFS;

/**
 * Created by Joao Raimundo on 29/03/2018.
 */

public class SDKPoAService extends Service {

    static final String TAG = SDKPoAService.class.getSimpleName();

    /**
     * Target we publish for clients to send messages to IncomingHandler.Note
     * that calls to its binder are sequential!
     */
    final Messenger serviceMessenger = new Messenger(new IncomingHandler());

    boolean isBound = false;

    /**
     * When binding to the service, we return an interface to our messenger for
     * sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        isBound = true;
        return serviceMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isBound = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isBound && intent != null) {
            if (intent.hasExtra(PARAM_WALLET_PACKAGE_NAME)) {
                // intent received that contains the wallet that answered to our brodcast
                // TODO Add logic to handle possible multiple intents received.
                SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(PREFERENCE_WALLET_PCKG_NAME, intent.getStringExtra(PARAM_WALLET_PACKAGE_NAME));
                editor.commit();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Handler of incoming messages from wallet applications.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "Message received: ");
            switch (msg.what) {
                case MSG_SIGN_PROOF:
                    Log.d(TAG, "MSG_SIGN_PROOF");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

}
