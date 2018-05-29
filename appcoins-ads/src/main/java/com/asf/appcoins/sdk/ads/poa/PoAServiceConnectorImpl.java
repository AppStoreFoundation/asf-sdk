package com.asf.appcoins.sdk.ads.poa;

/**
 * Created by Joao Raimundo on 28/03/2018.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import net.grandcentrix.tray.AppPreferences;

public class PoAServiceConnectorImpl implements PoAServiceConnector {

  private static final String TAG = PoAServiceConnectorImpl.class.getSimpleName();
  /** Flag indicating whether the connector is bound to the service. */
  private static boolean isBound;
  /**
   * Target we publish for clients to send messages to IncomingHandler. Note
   * that calls to its binder are sequential!
   */
  private final IncomingHandler handler;
  /**
   * Handler thread to avoid running on the main thread (UI)
   */
  private final HandlerThread handlerThread;
  /** Messenger for sending messages to the service. */
  private Messenger serviceMessenger = null;
  /** Messenger for receiving messages from the service. */
  private Messenger clientMessenger = null;
  /** Lists of messages that are pending to be send */
  private ArrayList<Message> pendingMsgsList = new ArrayList<>();
  /**
   * Class for interacting with the main interface of the service.
   */
  private ServiceConnection mConnection = new ServiceConnection() {
    @Override public void onServiceConnected(ComponentName className, IBinder service) {
      serviceMessenger = new Messenger(service);
      isBound = true;

      // send the pending messages that may have been added to the list before the bind was complete
      sendPendingMessages();
    }

    @Override public void onServiceDisconnected(ComponentName className) {
      // This is called when the connection with the service has been
      // unexpectedly disconnected -- that is, its process crashed.
      serviceMessenger = null;
      isBound = false;
    }
  };

  /**
   * The constructor for the connector implementation.
   */
  public PoAServiceConnectorImpl(ArrayList<MessageListener> listeners) {
    handlerThread = new HandlerThread("HandlerThread");
    handlerThread.start();
    handler = new IncomingHandler(handlerThread, listeners);
    clientMessenger = new Messenger(handler);
  }

  /**
   * Method to send implicit broadcast of the given intent. This method will check which packages
   * are ready to receive the broadcasts for the given intent and then send a explicit broadcast.
   *
   * @param context The application context
   * @param intent The intent to broadcast
   */
  private static void sendImplicitBroadcast(Context context, Intent intent) {
    PackageManager pm = context.getPackageManager();
    List<ResolveInfo> matches = pm.queryBroadcastReceivers(intent, 0);

    for (ResolveInfo resolveInfo : matches) {
      Intent explicit = new Intent(intent);
      ComponentName cn = new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName,
          resolveInfo.activityInfo.name);

      explicit.setComponent(cn);
      context.sendBroadcast(explicit);
    }
  }

  @Override public void startHandshake(Context context, int networkId) {
    Log.d(TAG, "Start handshake...");

    // send broadcast intent to start the handshake
    Intent broadcastIntent = new Intent(ACTION_START_HANDSHAKE);
    broadcastIntent.putExtra(PARAM_APP_PACKAGE_NAME, context.getPackageName());
    broadcastIntent.putExtra(PARAM_APP_SERVICE_NAME, SDKPoAService.class.getName());
    broadcastIntent.putExtra(PARAM_NETWORK_ID, networkId);
    // We need to start the handshake with the implicit broadcast, instead of a generic one due to a
    // 'ban' on the implicit broadcast when targeting Android 8.0 sdk (targetSdkVersion). Meaning
    // that only explicit broadcast will work. For that reason we search for the packages that can
    // listen to the intent that we intend to send and sent an explicit broadcast for it.
    sendImplicitBroadcast(context, broadcastIntent);
  }

  @Override public boolean connectToService(Context context) {
    if (isBound) {
      return true;
    }
    // Note that this is an implicit Intent that must be defined in the Android Manifest.
    final AppPreferences appPreferences =
        new AppPreferences(context); // this Preference comes for free from the library
    final String packageName = appPreferences.getString(PREFERENCE_WALLET_PCKG_NAME, null);

    boolean result = false;
    if (!TextUtils.isEmpty(packageName)) {
      Intent i = new Intent(ACTION_BIND);
      i.setPackage(packageName);

      result = context.getApplicationContext()
          .bindService(i, mConnection, Context.BIND_AUTO_CREATE);

      if (!result) {
        context.getApplicationContext()
            .unbindService(mConnection);
      }
    }

    return result;
  }

  @Override public void disconnectFromService(Context context) {
    if (isBound) {
      context.getApplicationContext()
          .unbindService(mConnection);
      isBound = false;
      pendingMsgsList = new ArrayList<>();
    }
  }

  @Override public void sendMessage(Context context, int type, Bundle bundle) {
    Log.d(TAG, "Send message to service of type: " + type + " with bundle: " + bundle);
    // Create and send a message to the service, using a supported 'what' value
    Message msg = Message.obtain(null, type, 0, 0);
    msg.setData(bundle);
    msg.replyTo = clientMessenger;

    synchronized (pendingMsgsList) {
      // validate if the service is bound
      if (!isBound) {
        connectToService(context);
        pendingMsgsList.add(msg);
      } else {
        if (pendingMsgsList.isEmpty()) {
          try {
            serviceMessenger.send(msg);
          } catch (RemoteException e) {
            Log.e(TAG, "Failed to send message: " + e.getMessage(), e);
          }
        } else {
          pendingMsgsList.add(msg);
          sendPendingMessages();
        }
      }
    }
  }

  /**
   * Method to send all messages that are pending.
   */
  private synchronized void sendPendingMessages() {
    Log.d(TAG, "Sending pending messages: " + pendingMsgsList.size());
    if (!pendingMsgsList.isEmpty()) {
      while (pendingMsgsList.size() > 0) {
        Message msg = pendingMsgsList.remove(0);
        try {
          Log.e(TAG, "Send message: " + msg.getData());
          serviceMessenger.send(msg);
        } catch (RemoteException e) {
          Log.e(TAG, "Failed to send message: " + e.getMessage(), e);
        }
      }
    }
  }

  /**
   * Handler of incoming messages from service.
   */
  class IncomingHandler extends Handler {

    ArrayList<MessageListener> listeners;

    public IncomingHandler(HandlerThread thr, ArrayList<MessageListener> listeners) {
      super(thr.getLooper());
      this.listeners = listeners;
    }

    @Override public void handleMessage(Message msg) {
      Log.d(TAG, "Message received: ");
      if (listeners != null) {
        for (MessageListener listener : listeners) {
          listener.handle(msg);
        }
      }
    }
  }
}

