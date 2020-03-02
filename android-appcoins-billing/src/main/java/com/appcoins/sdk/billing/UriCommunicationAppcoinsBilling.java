package com.appcoins.sdk.billing;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.appcoins.billing.AppcoinsBilling;
import com.appcoins.communication.SyncIpcMessageRequester;
import java.io.Serializable;

public class UriCommunicationAppcoinsBilling implements AppcoinsBilling, Serializable {
  private final SyncIpcMessageRequester messageRequester;

  public UriCommunicationAppcoinsBilling(SyncIpcMessageRequester messageRequester) {
    this.messageRequester = messageRequester;
  }

  @Override public int isBillingSupported(int apiVersion, String packageName, String type)
      throws RemoteException {
    Bundle arguments = new Bundle();
    arguments.putInt("API_VERSION", apiVersion);
    arguments.putString("PACKAGE_NAME", packageName);
    arguments.putString("BILLING_TYPE", type);

    return callMethod(0, arguments).getInt("RESULT_VALUE");
  }

  @Override
  public Bundle getSkuDetails(int apiVersion, String packageName, String type, Bundle skusBundle)
      throws RemoteException {
    Bundle arguments = new Bundle();
    arguments.putInt("API_VERSION", apiVersion);
    arguments.putString("PACKAGE_NAME", packageName);
    arguments.putString("BILLING_TYPE", type);
    arguments.putParcelable("SKUS_BUNDLE", skusBundle);
    return callMethod(1, arguments).getBundle("RESULT_VALUE");
  }

  @Override public Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type,
      String developerPayload) throws RemoteException {
    Bundle arguments = new Bundle();
    arguments.putInt("API_VERSION", apiVersion);
    arguments.putString("PACKAGE_NAME", packageName);
    arguments.putString("BILLING_SKU", sku);
    arguments.putString("BILLING_TYPE", type);
    arguments.putString("DEVELOPER_PAYLOAD", developerPayload);
    return callMethod(3, arguments).getBundle("RESULT_VALUE");
  }

  @Override public Bundle getPurchases(int apiVersion, String packageName, String type,
      String continuationToken) throws RemoteException {
    Bundle arguments = new Bundle();
    arguments.putInt("API_VERSION", apiVersion);
    arguments.putString("PACKAGE_NAME", packageName);
    arguments.putString("BILLING_TYPE", type);
    arguments.putString("CONTINUATION_TOKEN", continuationToken);
    return callMethod(2, arguments).getBundle("RESULT_VALUE");
  }

  @Override public int consumePurchase(int apiVersion, String packageName, String purchaseToken)
      throws RemoteException {
    Bundle arguments = new Bundle();
    arguments.putInt("API_VERSION", apiVersion);
    arguments.putString("PACKAGE_NAME", packageName);
    arguments.putString("PURCHASE_TOKEN", purchaseToken);
    return callMethod(4, arguments).getInt("RESULT_VALUE");
  }

  private Bundle callMethod(int methodId, Bundle arguments) throws RemoteException {
    try {
      return ((Bundle) messageRequester.sendMessage(methodId, arguments));
    } catch (Exception e) {
      throw new RemoteException(e.getMessage());
    }
  }

  @Override public IBinder asBinder() {
    return null;
  }
}
