package com.appcoins.sdk.billing.helpers;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import com.appcoins.billing.AppcoinsBilling;
import com.appcoins.sdk.android.billing.BuildConfig;
import com.appcoins.sdk.billing.ResponseCode;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.SkuDetailsResult;
import com.appcoins.sdk.billing.WSServiceController;
import java.util.ArrayList;
import java.util.List;

public class AppcoinsBillingStubHelper implements AppcoinsBilling {
  private static final String TAG = AppcoinsBillingStubHelper.class.getSimpleName();

  private final Object lockThread;
  private static AppcoinsBilling serviceAppcoinsBilling;
  private boolean isServiceBound = false;
  private boolean isMainThread;

  public AppcoinsBillingStubHelper() {
    this.lockThread = new Object();
  }

  @Override public int isBillingSupported(int apiVersion, String packageName, String type) {

    if (WalletUtils.hasWalletInstalled()) {
      try {
        synchronized (lockThread) {
          if (!isServiceBound) {
            createRepository();
            lockThread.wait();
          }
        }
        return serviceAppcoinsBilling.isBillingSupported(apiVersion, packageName, type);
      } catch (RemoteException | InterruptedException e) {
        e.printStackTrace();
        return ResponseCode.SERVICE_UNAVAILABLE.getValue();
      }
    } else {
      if (type.equalsIgnoreCase("inapp")) {
        return ResponseCode.OK.getValue();
      } else {
        return ResponseCode.BILLING_UNAVAILABLE.getValue();
      }
    }
  }

  @Override
  public Bundle getSkuDetails(final int apiVersion, final String packageName, final String type,
      final Bundle skusBundle) {

    Bundle responseWs = new Bundle();
    if (WalletUtils.hasWalletInstalled()) {
      try {
        synchronized (lockThread) {
          if (!isServiceBound) {
            createRepository();
            lockThread.wait();
          }
        }
        return serviceAppcoinsBilling.getSkuDetails(apiVersion, packageName, type, skusBundle);
      } catch (RemoteException | InterruptedException e) {
        e.printStackTrace();
        responseWs.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
      }
    } else {
      List<String> sku = skusBundle.getStringArrayList(Utils.GET_SKU_DETAILS_ITEM_LIST);
      String response =
          WSServiceController.getSkuDetailsService("https://api.blockchainds.com", packageName,
              sku);
      responseWs.putInt(Utils.RESPONSE_CODE, 0);
      ArrayList<String> skuDetails = buildResponse(response, type);
      responseWs.putStringArrayList("DETAILS_LIST", skuDetails);
    }
    return responseWs;
  }

  private ArrayList<String> buildResponse(String response, String type) {
    SkuDetailsResult skuDetailsResult = AndroidBillingMapper.mapSkuDetailsFromWS(type, response);
    ArrayList<String> list = new ArrayList<>();
    for (SkuDetails skuDetails : skuDetailsResult.getSkuDetailsList()) {
      list.add(map(skuDetails));
    }
    return list;
  }

  private String map(SkuDetails skuDetails) {
    return "{\"productId\":\""
        + skuDetails.getSku()
        + "\",\"type\" : \""
        + skuDetails.getType()
        + "\",\"price\" : "
        + skuDetails.getPrice()
        + ",\"price_currency_code\": \""
        + skuDetails.getPriceCurrencyCode()
        + "\",\"price_amount_micros\": "
        + skuDetails.getPriceAmountMicros()
        + ",\"title\" : \""
        + skuDetails.getTitle()
        + "\",\"description\" : \""
        + skuDetails.getDescription()
        + "\"}";
  }

  @Override public Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type,
      String developerPayload) {
    if (WalletUtils.hasWalletInstalled()) {
      isMainThread = Looper.myLooper() == Looper.getMainLooper();

      try {
        synchronized (lockThread) {
          if (!isServiceBound) {
            createRepository();
            if (!isMainThread) {
              lockThread.wait();
            } else {
              Bundle response = new Bundle();
              response.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
              return response;
            }
          }
        }
        return serviceAppcoinsBilling.getBuyIntent(apiVersion, packageName, sku, type,
            developerPayload);
      } catch (RemoteException | InterruptedException e) {
        e.printStackTrace();
        Bundle response = new Bundle();
        response.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
        return response;
      }
    } else {
      Intent intent = new Intent(WalletUtils.context.get(), InstallDialogActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(WalletUtils.context.get(), 0, intent,
          PendingIntent.FLAG_UPDATE_CURRENT);
      Bundle response = new Bundle();
      response.putParcelable("BUY_INTENT", pendingIntent);

      response.putInt(Utils.RESPONSE_CODE, ResponseCode.OK.getValue());
      return response;
    }
  }

  @Override public Bundle getPurchases(int apiVersion, String packageName, String type,
      String continuationToken) {
    Bundle bundleResponse = new Bundle();
    if (WalletUtils.hasWalletInstalled()) {
      try {

        synchronized (lockThread) {
          if (!isServiceBound) {
            createRepository();
            lockThread.wait();
          }
        }

        return serviceAppcoinsBilling.getPurchases(apiVersion, packageName, type, null);
      } catch (RemoteException | InterruptedException e) {
        e.printStackTrace();
        bundleResponse.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
      }
    } else {

      bundleResponse.putInt(Utils.RESPONSE_CODE, ResponseCode.OK.getValue());
      bundleResponse.putStringArrayList("INAPP_PURCHASE_ITEM_LIST", new ArrayList<String>());
      bundleResponse.putStringArrayList("INAPP_PURCHASE_DATA_LIST", new ArrayList<String>());
      bundleResponse.putStringArrayList("INAPP_DATA_SIGNATURE_LIST", new ArrayList<String>());
    }

    return bundleResponse;
  }

  @Override public int consumePurchase(int apiVersion, String packageName, String purchaseToken) {

    if (WalletUtils.hasWalletInstalled()) {
      try {
        synchronized (lockThread) {
          if (!isServiceBound) {
            createRepository();
            lockThread.wait();
          }
        }
        return serviceAppcoinsBilling.consumePurchase(apiVersion, packageName, purchaseToken);
      } catch (RemoteException | InterruptedException e) {
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

    Intent serviceIntent = new Intent(BuildConfig.IAB_BIND_ACTION);
    serviceIntent.setPackage(BuildConfig.IAB_BIND_PACKAGE);

    final Context context = WalletUtils.getActivity();

    List<ResolveInfo> intentServices = context.getPackageManager()
        .queryIntentServices(serviceIntent, 0);
    if (intentServices != null && !intentServices.isEmpty()) {
      context.bindService(serviceIntent, new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder service) {
          synchronized (lockThread) {
            serviceAppcoinsBilling = Stub.asInterface(service);
            lockThread.notify();
            isServiceBound = true;
            if (isMainThread) {
              Toast.makeText(context, "Try again, it will work this time =)", Toast.LENGTH_SHORT)
                  .show();
            }
            isMainThread = false;
            Log.d(TAG, "onServiceConnected() called service = [" + serviceAppcoinsBilling + "]");
          }
        }

        @Override public void onServiceDisconnected(ComponentName name) {
          Log.d(TAG, "onServiceDisconnected() called = [" + name + "]");
        }
      }, Context.BIND_AUTO_CREATE);
    }
  }

  public static abstract class Stub {

    public static AppcoinsBilling asInterface(IBinder service) {
      if (!WalletUtils.hasWalletInstalled()) {
        return new AppcoinsBillingStubHelper();
      } else {
        return AppcoinsBilling.Stub.asInterface(service);
      }
    }
  }
}