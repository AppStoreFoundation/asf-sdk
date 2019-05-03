package com.appcoins.sdk.billing.helpers;

import android.os.Bundle;
import android.os.IBinder;
import com.appcoins.billing.AppcoinsBilling;
import com.appcoins.sdk.billing.ResponseCode;
import com.appcoins.sdk.billing.WSServiceController;
import java.util.ArrayList;

public class AppcoinsBillingStubHelper extends android.os.Binder implements AppcoinsBilling {

  @Override public int isBillingSupported(int apiVersion, String packageName, String type) {
    return 0;
  }

  @Override
  public Bundle getSkuDetails(int apiVersion, String packageName, String type, Bundle skusBundle) {
    ArrayList<String> sku = (ArrayList<String>) skusBundle.get(Utils.GET_SKU_DETAILS_ITEM_LIST);
    String response = WSServiceController.GetSkuDetailsService(packageName, sku);
    Bundle responseWs = new Bundle();
    responseWs.putString(Utils.NO_WALLET_SKU_DETAILS, response);
    return responseWs;
  }

  @Override public Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type,
      String developerPayload) {
    Bundle response = new Bundle();
    return response;
  }

  @Override public Bundle getPurchases(int apiVersion, String packageName, String type,
      String continuationToken) {

    Bundle bundleResponse = new Bundle();

    bundleResponse.putInt(Utils.RESPONSE_CODE, ResponseCode.OK.getValue());
    bundleResponse.putStringArrayList(Utils.RESPONSE_INAPP_PURCHASE_DATA_LIST,
        new ArrayList<String>());
    bundleResponse.putStringArrayList(Utils.RESPONSE_INAPP_SIGNATURE_LIST, new ArrayList<String>());
    bundleResponse.putStringArrayList(Utils.RESPONSE_INAPP_PURCHASE_ID_LIST,
        new ArrayList<String>());

    return bundleResponse;
  }

  @Override public int consumePurchase(int apiVersion, String packageName, String purchaseToken) {
    return ResponseCode.OK.getValue();
  }

  @Override public IBinder asBinder() {
    return null;
  }

  public static abstract class Stub {

    public static AppcoinsBilling AsInterface(IBinder service) {
      if (!WalletUtils.hasWalletInstalled()) {
        return new AppcoinsBillingStubHelper();
      } else {
        return AppcoinsBilling.Stub.asInterface(service);
      }
    }
  }
}
