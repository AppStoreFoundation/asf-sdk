package com.appcoins.sdk.billing.helpers;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.appcoins.billing.AppcoinsBilling;
import com.appcoins.sdk.billing.ConnectionLifeCycle;
import com.appcoins.sdk.billing.LaunchBillingFlowResult;
import com.appcoins.sdk.billing.PurchasesResult;
import com.appcoins.sdk.billing.Repository;
import com.appcoins.sdk.billing.ResponseCode;
import com.appcoins.sdk.billing.SkuDetailsResult;
import com.appcoins.sdk.billing.exceptions.ServiceConnectionException;
import com.appcoins.sdk.billing.listeners.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.service.WalletBillingService;
import java.util.List;

class AppCoinsAndroidBillingRepository implements Repository, ConnectionLifeCycle {
  private final int apiVersion;
  private final String packageName;
  private AppcoinsBilling service;
  private boolean isServiceReady;

  public AppCoinsAndroidBillingRepository(int apiVersion, String packageName) {
    this.apiVersion = apiVersion;
    this.packageName = packageName;
  }

  @Override public void onConnect(ComponentName name, IBinder service,
      final AppCoinsBillingStateListener listener) {
    this.service = new WalletBillingService(service, name.getClassName());
    isServiceReady = true;
    listener.onBillingSetupFinished(ResponseCode.OK.getValue());
  }

  @Override public void onDisconnect(final AppCoinsBillingStateListener listener) {
    service = null;
    isServiceReady = false;
    listener.onBillingServiceDisconnected();
  }

  @Override public PurchasesResult getPurchases(String skuType) throws ServiceConnectionException {

    if (!isReady()) {
      throw new ServiceConnectionException();
    }
    try {
      Bundle purchases = service.getPurchases(apiVersion, packageName, skuType, null);
      PurchasesResult purchasesResult = AndroidBillingMapper.mapPurchases(purchases, skuType);

      return purchasesResult;
    } catch (RemoteException e) {
      e.printStackTrace();
      throw new ServiceConnectionException(e.getMessage());
    }
  }

  @Override
  public SkuDetailsResult querySkuDetailsAsync(final String skuType, final List<String> sku)
      throws ServiceConnectionException {

    if (!isReady()) {
      throw new ServiceConnectionException();
    }

    Bundle bundle = AndroidBillingMapper.mapArrayListToBundleSkuDetails(sku);

    Bundle response;

    try {
      response = service.getSkuDetails(apiVersion, packageName, skuType, bundle);
      return AndroidBillingMapper.mapBundleToHashMapSkuDetails(skuType, response);
    } catch (RemoteException e) {
      e.printStackTrace();
      throw new ServiceConnectionException(e.getMessage());
    }
  }

  @Override public int consumeAsync(String purchaseToken) throws ServiceConnectionException {

    if (!isReady()) {
      throw new ServiceConnectionException();
    }
    try {
      return service.consumePurchase(apiVersion, packageName, purchaseToken);
    } catch (RemoteException e) {
      e.printStackTrace();
      throw new ServiceConnectionException(e.getMessage());
    }
  }

  @Override
  public LaunchBillingFlowResult launchBillingFlow(String skuType, String sku, String payload)
      throws ServiceConnectionException {

    if (!isReady()) {
      throw new ServiceConnectionException();
    }
    try {
      Bundle response = service.getBuyIntent(apiVersion, packageName, sku, skuType, payload);

      return AndroidBillingMapper.mapBundleToHashMapGetIntent(response);
    } catch (RemoteException e) {
      e.printStackTrace();
      throw new ServiceConnectionException(e.getMessage());
    }
  }

  @Override public boolean isReady() {
    return isServiceReady;
  }
}
