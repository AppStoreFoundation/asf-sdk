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
import com.appcoins.billing.AppcoinsBilling;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.communication.SyncIpcMessageRequester;
import com.appcoins.communication.requester.MessageRequesterFactory;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.ResponseCode;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.SkuDetailsResult;
import com.appcoins.sdk.billing.UriCommunicationAppcoinsBilling;
import com.appcoins.sdk.billing.WSServiceController;
import com.appcoins.sdk.billing.listeners.StartPurchaseAfterBindListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public final class AppcoinsBillingStubHelper implements AppcoinsBilling, Serializable {
  public final static String BUY_ITEM_PROPERTIES = "buy_item_properties";
  private static final String TAG = AppcoinsBillingStubHelper.class.getSimpleName();
  private final static String APPCOINS_BILLING_STUB_HELPER_INSTANCE =
      "appcoins_billing_stub_helper";
  public static final int MESSAGE_RESPONSE_WAIT_TIMEOUT = 15000;
  private static AppcoinsBilling serviceAppcoinsBilling;
  private static AppcoinsBillingStubHelper appcoinsBillingStubHelper;
  private static int MAX_SKUS_SEND_WS = 49; // 0 to 49

  private AppcoinsBillingStubHelper() {
    appcoinsBillingStubHelper = this;
  }

  static AppcoinsBillingStubHelper getInstance() {
    if (appcoinsBillingStubHelper == null) {
      appcoinsBillingStubHelper = new AppcoinsBillingStubHelper();
    }
    return appcoinsBillingStubHelper;
  }

  @Override public int isBillingSupported(int apiVersion, String packageName, String type) {

    if (WalletUtils.hasWalletInstalled()) {
      try {
        return serviceAppcoinsBilling.isBillingSupported(apiVersion, packageName, type);
      } catch (RemoteException e) {
        e.printStackTrace();
        return ResponseCode.SERVICE_UNAVAILABLE.getValue();
      }
    } else {
      if (type.equalsIgnoreCase("inapp")) {
        if (apiVersion == 3) {
          return ResponseCode.OK.getValue();
        } else {
          return ResponseCode.BILLING_UNAVAILABLE.getValue();
        }
      } else {
        return ResponseCode.BILLING_UNAVAILABLE.getValue();
      }
    }
  }

  @Override
  public Bundle getSkuDetails(final int apiVersion, final String packageName, final String type,
      final Bundle skusBundle) {
    final CountDownLatch latch = new CountDownLatch(1);
    final Bundle responseWs = new Bundle();
    if (WalletUtils.hasWalletInstalled()) {
      try {
        return serviceAppcoinsBilling.getSkuDetails(apiVersion, packageName, type, skusBundle);
      } catch (RemoteException e) {
        e.printStackTrace();
        responseWs.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
      }
    } else {
      if (Looper.myLooper() == Looper.getMainLooper()) {
        Thread t = new Thread(new Runnable() {
          @Override public void run() {
            getSkuDetailsFromService(packageName, type, skusBundle, responseWs);
            latch.countDown();
          }
        });
        t.start();
        try {
          latch.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
          responseWs.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
        }
      } else {
        getSkuDetailsFromService(packageName, type, skusBundle, responseWs);
      }
    }
    return responseWs;
  }

  @Override public Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type,
      String developerPayload) {
    if (WalletUtils.hasWalletInstalled()) {
      try {
        return serviceAppcoinsBilling.getBuyIntent(apiVersion, packageName, sku, type,
            developerPayload);
      } catch (RemoteException e) {
        e.printStackTrace();
        Bundle response = new Bundle();
        response.putInt(Utils.RESPONSE_CODE, ResponseCode.SERVICE_UNAVAILABLE.getValue());
        return response;
      }
    } else {
      BuyItemProperties buyItemProperties =
          new BuyItemProperties(apiVersion, packageName, sku, type, developerPayload);

      Context context = WalletUtils.getContext();

      Intent intent = new Intent(context, InstallDialogActivity.class);
      intent.putExtra(APPCOINS_BILLING_STUB_HELPER_INSTANCE, this);
      intent.putExtra(BUY_ITEM_PROPERTIES, buyItemProperties);

      PendingIntent pendingIntent =
          PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        return serviceAppcoinsBilling.getPurchases(apiVersion, packageName, type, null);
      } catch (RemoteException e) {
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
        return serviceAppcoinsBilling.consumePurchase(apiVersion, packageName, purchaseToken);
      } catch (RemoteException e) {
        e.printStackTrace();
        return ResponseCode.SERVICE_UNAVAILABLE.getValue();
      }
    } else {
      return ResponseCode.OK.getValue();
    }
  }

  private void getSkuDetailsFromService(String packageName, String type, Bundle skusBundle,
      Bundle responseWs) {
    List<String> sku = skusBundle.getStringArrayList(Utils.GET_SKU_DETAILS_ITEM_LIST);
    ArrayList<SkuDetails> skuDetailsList = requestSkuDetails(sku, packageName, type);
    SkuDetailsResult skuDetailsResult = new SkuDetailsResult(skuDetailsList, 0);
    responseWs.putInt(Utils.RESPONSE_CODE, 0);
    ArrayList<String> skuDetails = buildResponse(skuDetailsResult);
    responseWs.putStringArrayList("DETAILS_LIST", skuDetails);
  }

  private ArrayList<SkuDetails> requestSkuDetails(List<String> sku, String packageName,
      String type) {
    List<String> skuSendList = new ArrayList<>();
    ArrayList<SkuDetails> skuDetailsList = new ArrayList<>();

    for (int i = 1; i <= sku.size(); i++) {
      skuSendList.add(sku.get(i - 1));
      if (i % MAX_SKUS_SEND_WS == 0 || i == sku.size()) {
        String response =
            WSServiceController.getSkuDetailsService(BuildConfig.HOST_WS, packageName, skuSendList);
        skuDetailsList.addAll(AndroidBillingMapper.mapSkuDetailsFromWS(type, response));
        skuSendList.clear();
      }
    }
    return skuDetailsList;
  }

  private ArrayList<String> buildResponse(SkuDetailsResult skuDetailsResult) {
    ArrayList<String> list = new ArrayList<>();
    for (SkuDetails skuDetails : skuDetailsResult.getSkuDetailsList()) {
      list.add(AndroidBillingMapper.mapSkuDetailsResponse(skuDetails));
    }
    return list;
  }

  @Override public IBinder asBinder() {
    return null;
  }

  boolean createRepository(
      final StartPurchaseAfterBindListener startPurchaseAfterConnectionListener) {

    String packageName = WalletUtils.getBillingServicePackageName();
    String iabAction = WalletUtils.getIabAction();

    Intent serviceIntent = new Intent(iabAction);
    serviceIntent.setPackage(packageName);

    final Context context = WalletUtils.getContext();

    List<ResolveInfo> intentServices = context.getPackageManager()
        .queryIntentServices(serviceIntent, 0);
    if (intentServices != null && !intentServices.isEmpty()) {
      return context.bindService(serviceIntent, new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder service) {
          serviceAppcoinsBilling = Stub.asInterface(service, "");
          startPurchaseAfterConnectionListener.startPurchaseAfterBind();
          Log.d(TAG, "onServiceConnected() called service = [" + serviceAppcoinsBilling + "]");
        }

        @Override public void onServiceDisconnected(ComponentName name) {
          Log.d(TAG, "onServiceDisconnected() called = [" + name + "]");
        }
      }, Context.BIND_AUTO_CREATE);
    }
    return false;
  }

  public static abstract class Stub {

    public static AppcoinsBilling asInterface(IBinder service, String componentName) {
      if (!WalletUtils.hasWalletInstalled()) {
        return AppcoinsBillingStubHelper.getInstance();
      } else {
        if (UriCommunicationAppcoinsBilling.class.getSimpleName()
            .equals(componentName)) {
          SyncIpcMessageRequester messageRequester =
              MessageRequesterFactory.create(WalletUtils.getContext(),
                  BuildConfig.BDS_WALLET_PACKAGE_NAME,
                  "appcoins://billing/communication/processor/1",
                  "appcoins://billing/communication/requester/1", MESSAGE_RESPONSE_WAIT_TIMEOUT);
          return new UriCommunicationAppcoinsBilling(messageRequester);
        } else {
          return AppcoinsBilling.Stub.asInterface(service);
        }
      }
    }
  }
}