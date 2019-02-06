package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.appcoins.sdk.android_appcoins_billing.ConnectionLifeCycle;
import com.appcoins.sdk.android_appcoins_billing.service.WalletBillingService;
import com.appcoins.sdk.billing.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.PurchasesResult;
import com.appcoins.sdk.billing.Repository;
import com.appcoins.sdk.billing.ServiceConnectionException;
import java.util.HashMap;
import java.util.List;

class AppCoinsAndroidBillingRepository implements Repository, ConnectionLifeCycle {
  private final int apiVersion;
  private final String packageName;
  private final AndroidBillingMapper mapper;
  private WalletBillingService service;

  public AppCoinsAndroidBillingRepository(int apiVersion, String packageName,
      AndroidBillingMapper mapper) {
    this.apiVersion = apiVersion;
    this.packageName = packageName;
    this.mapper = mapper;
  }

  @Override public void onConnect(IBinder service, final AppCoinsBillingStateListener listener) {
    this.service = new WalletBillingService(service);
    listener.onBillingSetupFinished(Utils.BILLING_RESPONSE_RESULT_OK);
  }

  @Override public void onDisconnect(final AppCoinsBillingStateListener listener) {
    service = null;
    listener.onBillingSetupFinished(Utils.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE);
  }

  @Override public PurchasesResult getPurchases(String skuType) throws ServiceConnectionException {
    if (service == null) {
      throw new ServiceConnectionException();
    }
    try {
      Bundle purchases = service.getPurchases(apiVersion, packageName, skuType, null);
      PurchasesResult pr = mapper.map(purchases, skuType);
      Log.d("purchases Size", " mapper size "+ pr.getPurchases().size());

      return pr;
    } catch (RemoteException e) {
      e.printStackTrace();
      throw new ServiceConnectionException();
    }
  }

  @Override public HashMap<String, Object> querySkuDetailsAsync(String skuType, List<String> sku)
      throws ServiceConnectionException {
    if (service == null) {
      throw new ServiceConnectionException();
    }

    Bundle bundle = mapper.mapArrayListToBundleSkuDetails(sku);
    try {
      Bundle response = service.getSkuDetails(apiVersion, packageName, skuType, bundle);
      HashMap<String, Object> hashMap = mapper.mapBundleToHashMapSkuDetails(skuType, response);

      return hashMap;
    } catch (RemoteException e) {
      throw new ServiceConnectionException();
    }
  }

  @Override public int consumeAsync(String purchaseToken) throws ServiceConnectionException {
    if (service == null) {
      throw new ServiceConnectionException();
    }
    try {
      return service.consumePurchase(apiVersion, packageName, purchaseToken);
    } catch (RemoteException e) {
      e.printStackTrace();
      throw new ServiceConnectionException();
    }
  }

  @Override
  public HashMap<String, Object> launchBillingFlow(String skuType, String sku, String payload)
      throws ServiceConnectionException {
    if (service == null) {
      throw new ServiceConnectionException();
    }
    try {
      Bundle response = service.getBuyIntent(apiVersion, packageName, sku, skuType, payload);
      return mapper.mapBundleToHashMapGetIntent(response);
    } catch (RemoteException e) {
      e.printStackTrace();
      throw new ServiceConnectionException();
    }
  }
}
