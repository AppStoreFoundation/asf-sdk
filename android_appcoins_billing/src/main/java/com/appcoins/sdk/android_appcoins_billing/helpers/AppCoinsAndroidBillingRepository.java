package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.appcoins.sdk.android_appcoins_billing.ConnectionLifeCycle;
import com.appcoins.sdk.android_appcoins_billing.service.WalletBillingService;
import com.appcoins.sdk.billing.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.PurchasesResult;
import com.appcoins.sdk.billing.Repository;
import com.appcoins.sdk.billing.ServiceConnectionException;

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
      return mapper.map(purchases, skuType);
    } catch (RemoteException e) {
      throw new ServiceConnectionException();
    }
  }
}
