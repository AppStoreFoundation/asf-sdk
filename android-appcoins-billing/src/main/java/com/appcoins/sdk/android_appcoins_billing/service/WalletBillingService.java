package com.appcoins.sdk.android_appcoins_billing.service;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.appcoins.billing.AppcoinsBilling;

public class WalletBillingService implements AppcoinsBilling {

  private AppcoinsBilling service;

  public WalletBillingService(IBinder service) {
    this.service = AppcoinsBilling.Stub.asInterface(service);
  }

  @Override
  public Bundle getSkuDetails(int apiVersion, String packageName, String type, Bundle skusBundle)
      throws RemoteException {
    return service.getSkuDetails(apiVersion, packageName, type, skusBundle);
  }

  @Override public int isBillingSupported(int apiVersion, String packageName, String type)
      throws RemoteException {
    return service.isBillingSupported(apiVersion, packageName, type);
  }

  @Override public Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type,
      String developerPayload) throws RemoteException {
    return service.getBuyIntent(apiVersion, packageName, sku, type, developerPayload);
  }

  @Override public Bundle getPurchases(int apiVersion, String packageName, String skuType,
      String continuationToken) throws RemoteException {
    return service.getPurchases(apiVersion, packageName, skuType, continuationToken);
  }

  @Override public int consumePurchase(int apiVersion, String packageName, String purchaseToken)
      throws RemoteException {
    return service.consumePurchase(apiVersion, packageName, purchaseToken);
  }

  @Override public IBinder asBinder() {
    return null;
  }
}

