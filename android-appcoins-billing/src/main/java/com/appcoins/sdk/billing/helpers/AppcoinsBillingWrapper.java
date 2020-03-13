package com.appcoins.sdk.billing.helpers;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.appcoins.billing.AppcoinsBilling;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_DATA_SIGNATURE_LIST;
import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_PURCHASE_DATA_LIST;
import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_PURCHASE_ID_LIST;
import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_PURCHASE_ITEM_LIST;

class AppcoinsBillingWrapper implements AppcoinsBilling, Serializable {

  private final AppcoinsBilling appcoinsBilling;
  private final String walletId;
  private long timeout;

  AppcoinsBillingWrapper(AppcoinsBilling appcoinsBilling, String walletId, long timeout) {
    this.appcoinsBilling = appcoinsBilling;
    this.walletId = walletId;
    this.timeout = timeout;
  }

  @Override public int isBillingSupported(int apiVersion, String packageName, String type)
      throws RemoteException {
    return appcoinsBilling.isBillingSupported(apiVersion, packageName, type);
  }

  @Override
  public Bundle getSkuDetails(int apiVersion, String packageName, String type, Bundle skusBundle)
      throws RemoteException {
    return appcoinsBilling.getSkuDetails(apiVersion, packageName, type, skusBundle);
  }

  @Override public Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type,
      String developerPayload) throws RemoteException {
    return appcoinsBilling.getBuyIntent(apiVersion, packageName, sku, type, developerPayload);
  }

  @Override public Bundle getPurchases(int apiVersion, String packageName, String type,
      String continuationToken) throws RemoteException {
    Bundle bundle = appcoinsBilling.getPurchases(apiVersion, packageName, type, continuationToken);
    if (walletId != null) {
      ArrayList<String> idsList = bundle.getStringArrayList(INAPP_PURCHASE_ID_LIST);
      ArrayList<String> skuList = bundle.getStringArrayList(INAPP_PURCHASE_ITEM_LIST);
      ArrayList<String> dataList = bundle.getStringArrayList(INAPP_PURCHASE_DATA_LIST);
      ArrayList<String> signatureDataList = bundle.getStringArrayList(INAPP_DATA_SIGNATURE_LIST);
      GuestPurchasesInteract guestPurchasesInteract = new GuestPurchasesInteract();
      CountDownLatch countDownLatch = new CountDownLatch(1);
      guestPurchasesInteract.mapGuestPurchases(bundle, walletId, packageName, type, countDownLatch,
          idsList, skuList, dataList, signatureDataList);
      waitForPurchases(countDownLatch);
    }
    return bundle;
  }

  @Override public int consumePurchase(int apiVersion, String packageName, String purchaseToken)
      throws RemoteException {
    return appcoinsBilling.consumePurchase(apiVersion, packageName, purchaseToken);
  }

  @Override public IBinder asBinder() {
    return appcoinsBilling.asBinder();
  }

  private void waitForPurchases(CountDownLatch countDownLatch) {
    try {
      countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
