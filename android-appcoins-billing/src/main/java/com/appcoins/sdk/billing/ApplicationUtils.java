package com.appcoins.sdk.billing;

import android.app.Activity;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import com.appcoins.sdk.billing.helpers.Utils;
import org.json.JSONObject;

public class ApplicationUtils {

  private static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
  private static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
  private static final String RESPONSE_INAPP_PURCHASE_ID = "INAPP_PURCHASE_ID";

  // IAB Helper error codes
  private static final int IABHELPER_VERIFICATION_FAILED = -1003;
  private static final int IABHELPER_USER_CANCELLED = -1005;
  private static final int IABHELPER_UNKNOWN_PURCHASE_RESPONSE = -1006;
  private static final int IABHELPER_UNKNOWN_ERROR = -1008;
  private static final int IABHELPER_BAD_RESPONSE = -1002;

  private final static String TAG = ApplicationUtils.class.getSimpleName();

  public static boolean handleActivityResult(Billing billing, int resultCode, Intent data,
      PurchaseFinishedListener purchaseFinishedListener) {

    if (data == null) {
      logError("Null data in IAB activity result.");
      purchaseFinishedListener.onPurchaseFinished(IABHELPER_UNKNOWN_ERROR,
          "Null data in IAB result", null, null);
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
        purchaseFinishedListener.onPurchaseFinished(IABHELPER_UNKNOWN_ERROR,
            "IAB returned null purchaseData or dataSignature", null, null);

        return false;
      }

      if (billing.verifyPurchase(purchaseData, Base64.decode(dataSignature, Base64.DEFAULT))) {
        JSONObject purchaseDataJSON = null;
        try {
          purchaseDataJSON = new JSONObject(purchaseData);
          purchaseFinishedListener.onPurchaseFinished(responseCode,
              "Purchase signature successfully verified.", getTokenFromJSON(purchaseDataJSON),
              getSkuFromJSON(purchaseDataJSON));
        } catch (Exception e) {
          e.printStackTrace();
          purchaseFinishedListener.onPurchaseFinished(IABHELPER_BAD_RESPONSE,
              "Failed to parse purchase data.", null, null);
          return false;
        }
        return true;
      } else {
        purchaseFinishedListener.onPurchaseFinished(IABHELPER_VERIFICATION_FAILED,
            "Signature verification failed for sku:", null, null);
        return false;
      }
    } else if (resultCode == Activity.RESULT_OK) {
      // result code was OK, but in-app billing response was not OK.
      logDebug("Result code was OK but in-app billing response was not OK: " + getResponseDesc(
          responseCode));
      purchaseFinishedListener.onPurchaseFinished(resultCode,
          "Result code was OK but in-app billing response was not OK: " + getResponseDesc(
              responseCode), null, null);
    } else if (resultCode == Activity.RESULT_CANCELED) {

      logDebug("Purchase canceled - Response: " + getResponseDesc(responseCode));
      purchaseFinishedListener.onPurchaseFinished(IABHELPER_USER_CANCELLED,
          "Purchase canceled - Response: " + getResponseDesc(responseCode), null, null);
    } else {
      logError("Purchase failed. Result code: " + resultCode + ". Response: " + getResponseDesc(
          responseCode));
      purchaseFinishedListener.onPurchaseFinished(IABHELPER_UNKNOWN_PURCHASE_RESPONSE,
          "Purchase canceled - Response: " + getResponseDesc(responseCode), null, null);
    }
    return true;
  }

  private static boolean verifySignature(String signature, String purchaseData,
      String dataSignature) {
    if (!Security.verifyPurchase(Base64.decode(signature, Base64.DEFAULT), purchaseData,
        Base64.decode(dataSignature, Base64.DEFAULT))) {
      logError("Purchase signature verification FAILED");
      return false;
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

  private static String getTokenFromJSON(JSONObject data) {
    return data.optString("purchaseToken");
  }

  private static String getSkuFromJSON(JSONObject data) {
    return data.optString("productId");
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
