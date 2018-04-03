package com.asf.appcoins.sdk.advertisement;

/**
 * Created by Joao Raimundo on 28/03/2018.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class PoAServiceConnectorImpl implements PoAServiceConnector {

    static final String TAG = "PoAServiceConnectorImpl";

    /** The instance for this singleton */
    private static PoAServiceConnectorImpl instance;

    /** Messenger for sending messages to the service. */
    Messenger serviceMessenger = null;
    /** Messenger for receiving messages from the service. */
    Messenger clientMessenger = null;

    /**
     * Target we publish for clients to send messages to IncomingHandler. Note
     * that calls to its binder are sequential!
     */
    private final IncomingHandler handler;

    /**
     * Handler thread to avoid running on the main thread (UI)
     */
    private final HandlerThread handlerThread;

    /** Flag indicating whether the connector is bound to the service. */
    static boolean isBound;

    /** Message that to be send as soon has we have the service bound */
    private static Message pendingMsg;

    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {

        public IncomingHandler(HandlerThread thr) {
            super(thr.getLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "Message received: ");

            switch (msg.what) {
                case MSG_REGISTER_CAMPAIGN:
                    Log.d(TAG, "MSG_REGISTER_CAMPAIGN");
                    break;
                case MSG_SEND_PROOF:
                    Log.d(TAG, "MSG_SEND_PROOF");
                    break;
                case MSG_SIGN_PROOF:
                    Log.d(TAG, "MSG_SIGN_PROOF");

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            serviceMessenger = new Messenger(service);

            isBound = true;

            if (pendingMsg != null) {
                try {
                    serviceMessenger.send(pendingMsg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            serviceMessenger = null;
            isBound = false;
        }
    };

    /**
     * Method to get the instance of the connector
     */
    public static PoAServiceConnectorImpl getInstance() {
        if (instance == null) {
            instance = new PoAServiceConnectorImpl();
        }
        return instance;
    }

    /**
     * The constructor for the connector implementation.
     */
    private PoAServiceConnectorImpl() {
        handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        handler = new IncomingHandler(handlerThread);
        clientMessenger = new Messenger(handler);
    }

    @Override
    public boolean connectToService(Context context, String action, String packageName) {
        // Note that this is an implicit Intent that must be defined in the Android Manifest.
        Intent i = new Intent(action);
        i.setPackage(packageName);

        return context.getApplicationContext().bindService(i, mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void disconnectFromService(Context context) {
        if (isBound) {
            context.getApplicationContext().unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    public void sendMessage(Context context, int type, Bundle bundle) {
        // Create a message to be sent to the service, using a supported 'what/type' value
        Message msg = Message.obtain(null, type, 0, 0);
        msg.setData(bundle);
        msg.replyTo = clientMessenger;

        // validate if the service is bound
        if (!isBound) {
            getService(context);
            pendingMsg = msg;
        } else {
            try {
                serviceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startHandshake(Context context) {
        // send broadcast intent to start the handshake
        Intent broadcastIntent = new Intent(ACTION_START_HANDSHAKE);
        broadcastIntent.putExtra(PARAM_APP_PACKAGE_NAME, context.getPackageName());
        broadcastIntent.putExtra(PARAM_APP_SERVICE_NAME, SDKPoAService.class.getName());
        context.sendBroadcast(broadcastIntent);
    }

    /**
     *  Method to get the service, by getting the wallet service package name obtained on the
     *  handshake.
     *
     *  @param context The context of the application.
     */
    private void getService(Context context) {
        if (!isBound) {
            Log.d(TAG, "Service no yet bound, getting package name fo the service. ");
            SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            String packageName = preferences.getString(PREFERENCE_WALLET_PCKG_NAME, null);

            Log.e(TAG, "Connecting to service on package: " + packageName);
            if (packageName != null) {
                connectToService(context, ACTION_BIND, packageName);
            }
        }
    }

}

