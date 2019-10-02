package com.asf.appcoins.sdk.ads.poa;

/**
 * Created by Joao Raimundo on 28/03/2018.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.asf.appcoins.sdk.ads.BuildConfig;
import com.asf.appcoins.sdk.ads.WalletPoAServiceListenner;
import com.asf.appcoins.sdk.ads.poa.manager.WalletUtils;
import java.util.ArrayList;

import static com.asf.appcoins.sdk.ads.poa.MessageListener.MSG_REGISTER_CAMPAIGN;

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
  private WalletPoAServiceListenner walletPoAServiceListenner;
  private int networkId;

  /**
   * Class for interacting with the main interface of the service.
   */
  private ServiceConnection mConnection = new ServiceConnection() {
    @Override public void onServiceConnected(ComponentName className, IBinder service) {
      serviceMessenger = new Messenger(service);
      isBound = true;
      walletPoAServiceListenner.isConnected();
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
  public PoAServiceConnectorImpl(ArrayList<MessageListener> listeners, int networkId) {
    handlerThread = new HandlerThread("HandlerThread");
    handlerThread.start();
    handler = new IncomingHandler(handlerThread, listeners);
    clientMessenger = new Messenger(handler);
    this.networkId = networkId;
  }

  @Override public void registerCampaign(Context context, String campaignId)
      throws RemoteException {
    Bundle bundle = new Bundle();
    bundle.putString("packageName", context.getPackageName());
    bundle.putString("campaignId", campaignId);

    Message msg = Message.obtain(null, MSG_REGISTER_CAMPAIGN, 0, 0);
    msg.setData(bundle);
    msg.replyTo = clientMessenger;
    serviceMessenger.send(msg);
  }

  @Override public boolean connectToService(Context context,
      WalletPoAServiceListenner walletPoAServiceListenner) {

    this.walletPoAServiceListenner = walletPoAServiceListenner;
    startWalletPoaService(context);

    boolean result = false;
    Intent i = new Intent(ACTION_BIND);
    i.setPackage(WalletUtils.getBillingServicePackageName());

    result = context.getApplicationContext()
        .bindService(i, mConnection, Context.BIND_AUTO_CREATE);

    if (!result) {
      context.getApplicationContext()
          .unbindService(mConnection);
    }
    return result;
  }

  @Override public boolean isConnectionReady() {
    return isBound;
  }

  public void startWalletPoaService(Context context) {
    Intent serviceIntent = new Intent();
    serviceIntent.setComponent(new ComponentName(BuildConfig.BDS_WALLET_PACKAGE_NAME,
        BuildConfig.APPCOINS_POA_SERVICE_NAME));
    serviceIntent.putExtra(PARAM_APP_PACKAGE_NAME, context.getPackageName());
    serviceIntent.putExtra(PARAM_APP_SERVICE_NAME, SDKPoAService.class.getName());
    serviceIntent.putExtra(PARAM_NETWORK_ID, networkId);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.startForegroundService(serviceIntent);
    } else {
      context.startService(serviceIntent);
    }
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

