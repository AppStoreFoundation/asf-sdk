package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import com.appcoins.sdk.billing.LaunchBillingFlowResult;
import com.appcoins.sdk.billing.Purchase;
import com.appcoins.sdk.billing.PurchasesResult;
import com.appcoins.sdk.billing.ResponseCode;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.SkuDetailsResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class AndroidBillingMapper {

  public static PurchasesResult mapPurchases(Bundle bundle, String skuType) {
    int responseCode = bundle.getInt("RESPONSE_CODE");
    ArrayList<String> purchaseDataList =
        bundle.getStringArrayList(Utils.RESPONSE_INAPP_PURCHASE_DATA_LIST);
    ArrayList<String> signatureList =
        bundle.getStringArrayList(Utils.RESPONSE_INAPP_SIGNATURE_LIST);
    ArrayList<String> idsList = bundle.getStringArrayList(Utils.RESPONSE_INAPP_PURCHASE_ID_LIST);

    List<Purchase> list = new ArrayList<>();

    for (int i = 0; i < purchaseDataList.size(); ++i) {
      String purchaseData = purchaseDataList.get(i);
      String signature = signatureList.get(i);
      String id = idsList.get(i);

      JSONObject jsonElement = null;
      try {
        jsonElement = new JSONObject(purchaseData);
        String orderId = jsonElement.getString("orderId");
        String packageName = jsonElement.getString("packageName");
        String sku = jsonElement.getString("productId");
        long purchaseTime = jsonElement.getLong("purchaseTime");
        int purchaseState = jsonElement.getInt("purchaseState");

        String developerPayload = null;
        try {
          if (jsonElement.getString("developerPayload") != null) {
            developerPayload = jsonElement.getString("developerPayload");
          }
        } catch (org.json.JSONException e) {
          Log.d("JSON:", " Field error" + e.getLocalizedMessage());
        }

        String token = null;
        try {
          if (jsonElement.getString("token") != null) {
            token = jsonElement.getString("token");
          }
        } catch (org.json.JSONException e) {
          Log.d("JSON:", " Field error " + e.getLocalizedMessage());
        }

        if (token == null) {
          token = jsonElement.getString("purchaseToken");
        }
        boolean isAutoRenewing = false;
        try {
          if (jsonElement.getString("autoRenewing") != null) {
            isAutoRenewing = jsonElement.getBoolean("autoRenewing");
          }
        } catch (org.json.JSONException e) {
          Log.d("JSON:", " Field error " + e.getLocalizedMessage());
        }
        //Base64 decoded string
        byte[] decodedSignature = Base64.decode(signature, Base64.DEFAULT);
        list.add(
            new Purchase(id, skuType, purchaseData, decodedSignature, purchaseTime, purchaseState,
                developerPayload, token, packageName, sku, isAutoRenewing));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    return new PurchasesResult(list, responseCode);
  }

  public static Bundle mapArrayListToBundleSkuDetails(List<String> skus) {
    Bundle bundle = new Bundle();
    bundle.putStringArrayList(Utils.GET_SKU_DETAILS_ITEM_LIST, (ArrayList<String>) skus);
    return bundle;
  }

  public static SkuDetailsResult mapBundleToHashMapSkuDetails(String skuType, Bundle bundle) {
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    ArrayList<SkuDetails> arrayList = new ArrayList<SkuDetails>();

    if (bundle.containsKey("DETAILS_LIST")) {
      ArrayList<String> responseList = bundle.getStringArrayList("DETAILS_LIST");
      for (String value : responseList) {
        SkuDetails skuDetails = parseSkuDetails(skuType, value);
        arrayList.add(skuDetails);
      }
    }
    int responseCode = (int) bundle.get("RESPONSE_CODE");
    SkuDetailsResult skuDetailsResult = new SkuDetailsResult(arrayList, responseCode);

    return skuDetailsResult;
  }

  public static SkuDetails parseSkuDetails(String skuType, String skuDetailsData) {
    try {
      JSONObject jsonElement = new JSONObject(skuDetailsData);

      String sku = jsonElement.getString("productId");
      String type = jsonElement.getString("type");
      String price = jsonElement.getString("price");
      Long priceAmountMicros = jsonElement.getLong("price_amount_micros");
      String priceCurrencyCode = jsonElement.getString("price_currency_code");
      String title = jsonElement.getString("title");
      String description = jsonElement.getString("description");

      return new SkuDetails(skuType, sku, type, price, priceAmountMicros, priceCurrencyCode, title,
          description);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return new SkuDetails(skuType, "", "", "", 0, "", "", "");
  }

  public static LaunchBillingFlowResult mapBundleToHashMapGetIntent(Bundle bundle) {

    LaunchBillingFlowResult launchBillingFlowResult =
        new LaunchBillingFlowResult(bundle.get("RESPONSE_CODE"),
            bundle.getParcelable("BUY_INTENT"));
    return launchBillingFlowResult;
  }

  public static SkuDetailsResult mapSkuDetailsFromWS(String skuType, String skuDetailsresponse,
      List<String> skus) {
    ArrayList<SkuDetails> arrayList = new ArrayList<SkuDetails>();

    if (skuDetailsresponse != "") {
      try {
        JSONObject jsonElement = new JSONObject(skuDetailsresponse);
        JSONArray items = jsonElement.getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
          JSONObject obj = items.getJSONObject(i);

          String sku = obj.getString("name");

          JSONObject priceObj = obj.getJSONObject("price");
          JSONObject fiat = priceObj.getJSONObject("fiat");

          String type = skuType;
          String price = fiat.getString("value");

          Long priceAmountMicros = priceObj.getLong("appc");

          String priceCurrencyCode = fiat.getJSONObject("currency")
              .getString("code");

          String title = obj.getString("label");

          String description = obj.getString("description");

          SkuDetails skuDetails =
              new SkuDetails(skuType, sku, type, price, priceAmountMicros, priceCurrencyCode, title,
                  description);

          arrayList.add(skuDetails);
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    return new SkuDetailsResult(arrayList, ResponseCode.OK.getValue());
  }
}
