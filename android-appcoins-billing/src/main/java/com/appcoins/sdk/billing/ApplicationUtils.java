package com.appcoins.sdk.billing;

import android.app.Activity;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import com.appcoins.sdk.billing.helpers.Utils;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

class ApplicationUtils {

  private static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
  private static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
  private static final String RESPONSE_INAPP_PURCHASE_ID = "INAPP_PURCHASE_ID";

  private final static String TAG = ApplicationUtils.class.getSimpleName();

  static boolean handleActivityResult(Billing billing, int resultCode, Intent data,
      PurchasesUpdatedListener purchaseFinishedListener) {

    if (data == null) {
      logError("Null data in IAB activity result.");
      purchaseFinishedListener.onPurchasesUpdated(ResponseCode.ERROR.getValue(),
          Collections.<Purchase>emptyList());
      return false;
    }

    int responseCode = getResponseCodeFromIntent(data);
    String purchaseData = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
    String dataSignature = data.getStringExtra(RESPONSE_INAPP_SIGNATURE);

    if (resultCode == Activity.RESULT_OK && responseCode == ResponseCode.OK.getValue()) {
      logDebug("Successful resultcode from purchase activity.");
      logDebug("Purchase data: " + purchaseData);
      logDebug("Data signature: " + dataSignature);
      logDebug("Extras: " + data.getExtras());

      if (purchaseData == null || dataSignature == null) {
        logError("BUG: either purchaseData or dataSignature is null.");
        logDebug("Extras: " + data.getExtras()
            .toString());
        purchaseFinishedListener.onPurchasesUpdated(ResponseCode.ERROR.getValue(),
            Collections.<Purchase>emptyList());

        return false;
      }

      if (billing.verifyPurchase(purchaseData, Base64.decode(dataSignature, Base64.DEFAULT))) {
        JSONObject purchaseDataJSON = null;
        try {
          purchaseDataJSON = new JSONObject(purchaseData);
          Purchase purchase =
              new Purchase(getObjectFromJson(purchaseDataJSON, "orderId"), "inapp", purchaseData,
                  Base64.decode(dataSignature, Base64.DEFAULT),
                  Long.parseLong(getObjectFromJson(purchaseDataJSON, "purchaseTime")),
                  Integer.decode(getObjectFromJson(purchaseDataJSON, "purchaseState")),
                  URLDecoder.decode(getObjectFromJson(purchaseDataJSON, "developerPayload"),
                      "utf-8"), getObjectFromJson(purchaseDataJSON, "purchaseToken"),
                  getObjectFromJson(purchaseDataJSON, "packageName"),
                  getObjectFromJson(purchaseDataJSON, "productId"),
                  Boolean.parseBoolean(getObjectFromJson(purchaseDataJSON, "isAutoRenewing")));
          List<Purchase> purchases = new ArrayList<>();
          purchases.add(purchase);
          purchaseFinishedListener.onPurchasesUpdated(responseCode, purchases);
        } catch (Exception e) {
          e.printStackTrace();
          purchaseFinishedListener.onPurchasesUpdated(ResponseCode.ERROR.getValue(),
              Collections.<Purchase>emptyList());
          logError("Failed to parse purchase data.");
          return false;
        }
        return true;
      } else {
        logError("Signature verification failed for sku:");
        purchaseFinishedListener.onPurchasesUpdated(ResponseCode.ERROR.getValue(),
            Collections.<Purchase>emptyList());
        return false;
      }
    } else if (resultCode == Activity.RESULT_OK) {
      // result code was OK, but in-app billing response was not OK.
      logDebug("Result code was OK but in-app billing response was not OK: " + getResponseDesc(
          responseCode));
      purchaseFinishedListener.onPurchasesUpdated(responseCode, Collections.<Purchase>emptyList());
    } else if (resultCode == Activity.RESULT_CANCELED) {

      logDebug("Purchase canceled - Response: " + getResponseDesc(responseCode));
      purchaseFinishedListener.onPurchasesUpdated(ResponseCode.USER_CANCELED.getValue(),
          Collections.<Purchase>emptyList());
    } else {
      logError("Purchase failed. Result code: " + resultCode + ". Response: " + getResponseDesc(
          responseCode));
      purchaseFinishedListener.onPurchasesUpdated(ResponseCode.ERROR.getValue(),
          Collections.<Purchase>emptyList());
    }
    return true;
  }

  private static int getResponseCodeFromIntent(Intent i) {
    Object o = i.getExtras()
        .get(Utils.RESPONSE_CODE);
    if (o == null) {
      logError("Intent with no response code, assuming OK (known issue)");
      return ResponseCode.OK.getValue();
    } else if (o instanceof Integer) {
      return ((Integer) o).intValue();
    } else if (o instanceof Long) {
      return (int) ((Long) o).longValue();
    } else {
      logError("Unexpected type for intent response code.");
      logError(o.getClass()
          .getName());
      throw new RuntimeException("Unexpected type for intent response code: " + o.getClass()
          .getName());
    }
  }

  private static void logDebug(String msg) {
    Log.d(TAG, msg);
  }

  private static void logError(String msg) {
    Log.e(TAG, "In-app billing error: " + msg);
  }

  private static String getObjectFromJson(JSONObject data, String objectId) {
    return data.optString(objectId);
  }

  private static String getResponseDesc(int code) {
    String[] iab_msgs = ("0:OK/1:User Canceled/2:Unknown/"
        + "3:Billing Unavailable/4:Item unavailable/"
        + "5:Developer Error/6:Error/7:Item Already Owned/"
        + "8:Item not owned").split("/");
    String[] iabhelper_msgs = ("0:OK/-1001:Remote exception during initialization/"
        + "-1002:Bad response received/"
        + "-1003:Purchase signature verification failed/"
        + "-1004:Send intent failed/"
        + "-1005:User cancelled/"
        + "-1006:Unknown purchase response/"
        + "-1007:Missing token/"
        + "-1008:Unknown error/"
        + "-1009:Subscriptions not available/"
        + "-1010:Invalid consumption attempt").split("/");

    if (code <= -1000) {
      int index = -1000 - code;
      if (index < iabhelper_msgs.length) {
        return iabhelper_msgs[index];
      } else {
        return code + ":Unknown IAB Helper Error";
      }
    } else if (code < 0 || code >= iab_msgs.length) {
      return code + ":Unknown";
    } else {
      return iab_msgs[code];
    }
  }
}
