package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.appcoins.sdk.android_appcoins_billing.ConnectionLifeCycle;
import com.appcoins.sdk.android_appcoins_billing.service.WalletBillingService;
import com.appcoins.sdk.billing.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.GetSkuDetailsService;
import com.appcoins.sdk.billing.LaunchBillingFlowResult;
import com.appcoins.sdk.billing.Purchase;
import com.appcoins.sdk.billing.PurchasesResult;
import com.appcoins.sdk.billing.Repository;
import com.appcoins.sdk.billing.ResponseCode;
import com.appcoins.sdk.billing.ServiceConnectionException;
import com.appcoins.sdk.billing.SkuDetailsResult;
import java.util.ArrayList;
import java.util.List;

class AppCoinsAndroidBillingRepository implements Repository, ConnectionLifeCycle {
  private final int apiVersion;
  private final String packageName;
  private final Context context;
  private WalletBillingService service;
  private GetSkuDetailsService getSkuDetailsService;

  public AppCoinsAndroidBillingRepository(int apiVersion, String packageName,
       Context context,
      GetSkuDetailsService getSkuDetailsService) {
    this.apiVersion = apiVersion;
    this.packageName = packageName;
    this.context = context;
    this.getSkuDetailsService = getSkuDetailsService;
  }

  @Override public void onConnect(IBinder service, final AppCoinsBillingStateListener listener) {
    if (!WalletUtils.hasWalletInstalled(context)) {
      listener.onBillingSetupFinished(Utils.BILLING_RESPONSE_RESULT_OK);
    } else {
      this.service = new WalletBillingService(service);
      listener.onBillingSetupFinished(Utils.BILLING_RESPONSE_RESULT_OK);
    }
  }

  @Override public void onDisconnect(final AppCoinsBillingStateListener listener) {
    if (!WalletUtils.hasWalletInstalled(context)) {
      listener.onBillingSetupFinished(Utils.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE);
    } else {
      service = null;
      listener.onBillingSetupFinished(Utils.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE);
    }
  }

  @Override public PurchasesResult getPurchases(String skuType) throws ServiceConnectionException {

    if (!WalletUtils.hasWalletInstalled(context)) {
      PurchasesResult purchasesResult =
          new PurchasesResult(new ArrayList<Purchase>(), ResponseCode.OK.getValue());
      return purchasesResult;
    }

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

  @Override public SkuDetailsResult querySkuDetailsAsync(String skuType, List<String> sku)
      throws ServiceConnectionException {

    if (!WalletUtils.hasWalletInstalled(context)) {
      String response = getSkuDetailsService.getSkuDetailsForPackageName(packageName, sku);
      SkuDetailsResult skuDetailsResult = AndroidBillingMapper.mapSkuDetailsFromWS(skuType, response, sku);
      return skuDetailsResult;
    }

    if (!isReady()) {
      throw new ServiceConnectionException();
    }

    Bundle bundle = AndroidBillingMapper.mapArrayListToBundleSkuDetails(sku);
    try {
      Bundle response = service.getSkuDetails(apiVersion, packageName, skuType, bundle);
      SkuDetailsResult skuDetailsResult =
          AndroidBillingMapper.mapBundleToHashMapSkuDetails(skuType, response);

      return skuDetailsResult;
    } catch (RemoteException e) {
      throw new ServiceConnectionException(e.getMessage());
    }
  }

  @Override public int consumeAsync(String purchaseToken) throws ServiceConnectionException {
    if (!WalletUtils.hasWalletInstalled(context)) {
      return ResponseCode.OK.getValue();
    }

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
    if (!WalletUtils.hasWalletInstalled(context)) {
      return true;
    }
    return service != null;
  }
}
