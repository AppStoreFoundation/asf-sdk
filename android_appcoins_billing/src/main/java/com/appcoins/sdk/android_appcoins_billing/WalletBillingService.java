package com.appcoins.sdk.android_appcoins_billing;


import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.appcoins.billing.AppcoinsBilling;;import java.util.ArrayList;

public class WalletBillingService implements AppcoinsBilling {

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
    public Bundle getPurchases(int apiVersion, String packageName, String skuType, String continuationToken) throws RemoteException {
        //return service.getPurchases(apiVersion, packageName, skuType, continuationToken);
        Bundle b = new Bundle();
        ArrayList<String> al = new ArrayList<String>();
        al.add("sku1");
        al.add("sku2");
        al.add("sku3");
        b.putStringArrayList(Utils.RESPONSE_INAPP_ITEM_LIST,al);
        b.putStringArrayList(Utils.RESPONSE_INAPP_PURCHASE_DATA_LIST,al);
        b.putStringArrayList(Utils.RESPONSE_INAPP_SIGNATURE_LIST,al);
        b.putStringArrayList(Utils.RESPONSE_INAPP_PURCHASE_ID_LIST,al);
        b.putInt(Utils.RESPONSE_CODE, Utils.BILLING_RESPONSE_RESULT_OK);
        return b;
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

