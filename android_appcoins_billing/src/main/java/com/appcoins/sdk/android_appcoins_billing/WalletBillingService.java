package com.appcoins.sdk.android_appcoins_billing;


import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.appcoins.billing.AppcoinsBilling;;

class WalletBillingService implements AppcoinsBilling {

    private AppcoinsBilling service;

    public WalletBillingService(IBinder service){
        this.service = AppcoinsBilling.Stub.asInterface(service);
    }

    @Override
     public Bundle getSkuDetails(int apiVersion, String packageName, String type, Bundle skusBundle) throws RemoteException {
        return service.getSkuDetails(apiVersion, packageName, type, skusBundle);
      }

    @Override
    public int isBillingSupported(int apiVersion, String packageName, String type) throws RemoteException {
        return service.isBillingSupported(apiVersion, packageName, type);
    }

    @Override
    public Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type, String developerPayload) throws RemoteException {
        return null;
    }

    @Override
    public Bundle getPurchases(int apiVersion, String packageName, String type, String continuationToken) throws RemoteException {
        return null;
    }

    @Override
    public int consumePurchase(int apiVersion, String packageName, String purchaseToken) throws RemoteException {
        return 0;
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
