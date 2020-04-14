package com.appcoins.sdk.billing;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper;
import com.appcoins.sdk.billing.helpers.BindType;
import com.appcoins.sdk.billing.helpers.IBinderWalletNotInstalled;
import com.appcoins.sdk.billing.helpers.WalletUtils;

public class WalletBinderUtil {

  public static final Object object = new Object();
  public static final int TIME_TO_WAIT_FOR_BIND_IN_MILLIS = 2000;
  public static boolean isTimeout = true;
  public static BindType bindType = BindType.AIDL;

  public static void walletNotInstalledBehaviour(ServiceConnection connection) {
    bindType = BindType.WALLET_NOT_INSTALLED;
    connection.onServiceConnected(
        new ComponentName("", AppcoinsBillingStubHelper.class.getSimpleName()),
        new IBinderWalletNotInstalled());
  }

  @SuppressLint("ObsoleteSdkInt")
  public static void bindFailedBehaviour(ServiceConnection connection) {
    if (BuildConfig.URI_COMMUNICATION
        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      bindType = BindType.URI_CONNECTION;
      connection.onServiceConnected(
          new ComponentName("", UriCommunicationAppcoinsBilling.class.getSimpleName()),
          new IBinderWalletNotInstalled());
    }
  }

  public static void onServiceConnected() {

    isTimeout = false;
    synchronized (object) {
      object.notifyAll();
    }
  }

  public static void walletInstalledBehaviour(Context context, final ServiceConnection connection,
      Intent serviceIntent, int serviceIntentFlags) {
    if (!context.bindService(serviceIntent, connection, serviceIntentFlags)) {
      bindFailedBehaviour(connection);
    } else {
      bindType = BindType.AIDL;
    }

    checkBind(connection);
  }

  private static void checkBind(final ServiceConnection connection) {
    new Thread(new Runnable() {
      @Override public void run() {
        synchronized (object) {
          try {
            object.wait(TIME_TO_WAIT_FOR_BIND_IN_MILLIS);
          } catch (InterruptedException e) {
            e.printStackTrace();
            bindFailedBehaviour(connection);
          }
        }
        if (isTimeout) {
          bindFailedBehaviour(connection);
        }
      }
    }).start();
  }

  public static void bindService(Context context, Intent serviceIntent,
      ServiceConnection connection, int serviceIntentFlags) {
    if (WalletUtils.hasWalletInstalled()) {
      walletInstalledBehaviour(context, connection, serviceIntent, serviceIntentFlags);
    } else {
      walletNotInstalledBehaviour(connection);
    }
  }
}
