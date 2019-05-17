package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.appcoins.billing.AppcoinsBilling;
import com.appcoins.sdk.billing.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.RepositoryServiceConnection;
import com.appcoins.sdk.billing.ResponseCode;
import com.appcoins.sdk.billing.WSServiceController;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AppcoinsBillingStubHelper implements AppcoinsBilling {

  private Object lockThread;
  private static AppcoinsBilling serviceAppcoinsBilling;
  private boolean wasServiceBinded = false;

  public AppcoinsBillingStubHelper() {
    this.lockThread = new Object();
  }

  @Override public int isBillingSupported(int apiVersion, String packageName, String type) {

    if (WalletUtils.hasWalletInstalled()) {
      try {
        if (!wasServiceBinded) {
          createRepository();
          lockThread.wait();
        }

        return serviceAppcoinsBilling.isBillingSupported(apiVersion, packageName, type);
      } catch (RemoteException e) {
        e.printStackTrace();
        return ResponseCode.SERVICE_UNAVAILABLE.getValue();
      } catch (InterruptedException e) {
        e.printStackTrace();
        return ResponseCode.SERVICE_UNAVAILABLE.getValue();
      }
    } else {
      return ResponseCode.OK.getValue();
    }
  }

  @Override public synchronized Bundle getSkuDetails(final int apiVersion, final String packageName,
      final String type, final Bundle skusBundle) {

    Bundle responseWs = new Bundle();
    if (WalletUtils.hasWalletInstalled()) {
      try {
        if (!wasServiceBinded) {
          createRepository();
          synchronized (lockThread) {
            lockThread.wait();
          }
        }
        return serviceAppcoinsBilling.getSkuDetails(apiVersion, packageName, type, skusBundle);
      } catch (RemoteException e) {
        e.printStackTrace();
        responseWs.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
      } catch (InterruptedException e) {
        e.printStackTrace();
        responseWs.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
      }
    } else {
      ArrayList<String> sku = (ArrayList<String>) skusBundle.get(Utils.GET_SKU_DETAILS_ITEM_LIST);
      String response = WSServiceController.GetSkuDetailsService(packageName, sku);
      responseWs.putString(Utils.NO_WALLET_SKU_DETAILS, response);
    }
    return responseWs;
  }

  @Override public synchronized Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type,
      String developerPayload) {
    if (WalletUtils.hasWalletInstalled()) {
      try {
        if (!wasServiceBinded) {
          createRepository();
          synchronized (lockThread) {
            lockThread.wait();
          }
        }
        return serviceAppcoinsBilling.getBuyIntent(apiVersion, packageName, sku, type,
            developerPayload);
      } catch (RemoteException e) {
        e.printStackTrace();
        Bundle response = new Bundle();
        response.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
        return response;
      } catch (InterruptedException e) {
        Bundle response = new Bundle();
        response.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
        return response;
      }
    } else {
      try {
        Activity act = WalletUtils.getActivity();
        act.runOnUiThread(new Runnable() {
          @Override public void run() {
            WalletUtils.promptToInstallWallet();
          }
        });
      } catch (Exception e) {
        Bundle response = new Bundle();
        response.putInt(Utils.RESPONSE_CODE, ResponseCode.ERROR.getValue());
        return response;
      }

      Bundle response = new Bundle();
      response.putString(Utils.HAS_WALLET_INSTALLED, "");
      response.putInt(Utils.RESPONSE_CODE, ResponseCode.OK.getValue());
      return response;
    }
  }

  @Override public Bundle getPurchases(int apiVersion, String packageName, String type,
      String continuationToken) {
    Bundle bundleResponse = new Bundle();
    if (WalletUtils.hasWalletInstalled()) {
      try {
        if (!wasServiceBinded) {
          createRepository();
          synchronized (lockThread) {
            lockThread.wait();
          }
        }
        return serviceAppcoinsBilling.getPurchases(apiVersion, packageName, type, null);
      } catch (RemoteException e) {
        e.printStackTrace();
        bundleResponse.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
      } catch (InterruptedException e) {
        e.printStackTrace();
        bundleResponse.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
      }
    } else {

      bundleResponse.putInt(Utils.RESPONSE_CODE, ResponseCode.OK.getValue());
      bundleResponse.putStringArrayList(Utils.RESPONSE_INAPP_PURCHASE_DATA_LIST,
          new ArrayList<String>());
      bundleResponse.putStringArrayList(Utils.RESPONSE_INAPP_SIGNATURE_LIST,
          new ArrayList<String>());
      bundleResponse.putStringArrayList(Utils.RESPONSE_INAPP_PURCHASE_ID_LIST,
          new ArrayList<String>());
    }

    return bundleResponse;
  }

  @Override public int consumePurchase(int apiVersion, String packageName, String purchaseToken) {

    if (WalletUtils.hasWalletInstalled()) {
      try {
        if (!wasServiceBinded) {
          createRepository();
          synchronized (lockThread) {
            lockThread.wait();
          }
        }
        return serviceAppcoinsBilling.consumePurchase(apiVersion, packageName, purchaseToken);
      } catch (RemoteException e) {
        e.printStackTrace();
        return ResponseCode.SERVICE_UNAVAILABLE.getValue();
      } catch (InterruptedException e) {
        e.printStackTrace();
        return ResponseCode.SERVICE_UNAVAILABLE.getValue();
      }
    } else {
      return ResponseCode.OK.getValue();
    }
  }

  @Override public IBinder asBinder() {
    return null;
  }

  private void createRepository() {
    final AppCoinsAndroidBillingRepository repository =
        new AppCoinsAndroidBillingRepository(Utils.API_VERSION, WalletUtils.walletPackageName);

    RepositoryServiceConnection connection =
        new RepositoryServiceConnection(WalletUtils.context, repository);

    connection.startConnection(new AppCoinsBillingStateListener() {
      @Override public void onBillingSetupFinished(int responseCode) {
        synchronized (lockThread) {
          lockThread.notify();
          wasServiceBinded = true;
        }
        Log.d(TAG,
            "onServiceConnected() called aervice = [" + serviceAppcoinsBilling + "]");

      }

      @Override public void onBillingServiceDisconnected() {
        wasServiceBinded = false;
      }
    });
  }

  public static abstract class Stub {

    public static AppcoinsBilling asInterface(IBinder service) {
      if (!WalletUtils.hasWalletInstalled()) {
        return new AppcoinsBillingStubHelper();
      } else {
        serviceAppcoinsBilling = AppcoinsBilling.Stub.asInterface(service);
        return serviceAppcoinsBilling;
      }
    }
  }
}