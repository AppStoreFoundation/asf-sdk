package com.appcoins.sdk.billing.helpers;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.appcoins.billing.AppcoinsBilling;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.payasguest.BillingRepository;
import com.appcoins.sdk.billing.service.BdsService;
import java.io.Serializable;
import java.util.ArrayList;

import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_DATA_SIGNATURE_LIST;
import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_PURCHASE_DATA_LIST;
import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_PURCHASE_ID_LIST;
import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_PURCHASE_ITEM_LIST;
import static com.appcoins.sdk.billing.payasguest.BillingRepository.RESPONSE_ERROR;
import static com.appcoins.sdk.billing.payasguest.BillingRepository.RESPONSE_SUCCESS;

class AppcoinsBillingWrapper implements AppcoinsBilling, Serializable {

  private final AppcoinsBilling appcoinsBilling;
  private final String walletId;
  private long timeoutInMillis;

  AppcoinsBillingWrapper(AppcoinsBilling appcoinsBilling, String walletId, long timeoutInMillis) {
    this.appcoinsBilling = appcoinsBilling;
    this.walletId = walletId;
    this.timeoutInMillis = timeoutInMillis;
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
      BillingRepository billingRepository =
          new BillingRepository(new BdsService(BuildConfig.HOST_WS, BdsService.TIME_OUT_IN_MILLIS));
      GuestPurchasesInteract guestPurchasesInteract = new GuestPurchasesInteract(billingRepository);
      bundle =
          guestPurchasesInteract.mapGuestPurchases(bundle, walletId, packageName, type, idsList,
              skuList, dataList, signatureDataList);
    }
    return bundle;
  }

  @Override public int consumePurchase(int apiVersion, String packageName, String purchaseToken)
      throws RemoteException {
    int responseCode = appcoinsBilling.consumePurchase(apiVersion, packageName, purchaseToken);
    int guestResponseCode = consumeGuestPurchase(walletId, apiVersion, packageName, purchaseToken);
    if (responseCode == RESPONSE_SUCCESS || guestResponseCode == RESPONSE_SUCCESS) {
      return RESPONSE_SUCCESS;
    } else {
      return RESPONSE_ERROR;
    }
  }

  @Override public IBinder asBinder() {
    return appcoinsBilling.asBinder();
  }

  private int consumeGuestPurchase(String walletId, int apiVersion, String packageName,
      String purchaseToken) {
    int responseCode = RESPONSE_ERROR;
    if (walletId != null && apiVersion == 3) {
      BillingRepository billingRepository =
          new BillingRepository(new BdsService(BuildConfig.HOST_WS, BdsService.TIME_OUT_IN_MILLIS));
      GuestPurchasesInteract guestPurchaseInteract = new GuestPurchasesInteract(billingRepository);
      responseCode =
          guestPurchaseInteract.consumeGuestPurchase(this.walletId, packageName, purchaseToken);
    }
    return responseCode;
  }
}
