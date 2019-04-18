package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.os.Bundle;
import android.util.Base64;
import com.appcoins.sdk.billing.LaunchBillingFlowResult;
import com.appcoins.sdk.billing.Purchase;
import com.appcoins.sdk.billing.PurchasesResult;
import com.appcoins.sdk.billing.ResponseCode;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.SkuDetailsResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class AndroidBillingMapper {
  private final JsonParser jsonParser;

  public AndroidBillingMapper(JsonParser jsonParser) {
    this.jsonParser = jsonParser;
  }

  public PurchasesResult mapPurchases(Bundle bundle, String skuType) {
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

      JsonObject jsonElement = jsonParser.parse(purchaseData)
          .getAsJsonObject();
      String orderId = jsonElement.get("orderId")
          .getAsString();
      String packageName = jsonElement.get("packageName")
          .getAsString();
      String sku = jsonElement.get("productId")
          .getAsString();
      long purchaseTime = jsonElement.get("purchaseTime")
          .getAsLong();
      int purchaseState = jsonElement.get("purchaseState")
          .getAsInt();

      String developerPayload = null;
      if (jsonElement.get("developerPayload") != null) {
        developerPayload = jsonElement.get("developerPayload")
            .getAsString();
      }
      String token = null;
      if (jsonElement.get("token") != null) {
        token = jsonElement.get("token")
            .getAsString();
      }

      if (token == null) {
        token = jsonElement.get("purchaseToken")
            .getAsString();
      }
      boolean isAutoRenewing = false;
      if (jsonElement.get("autoRenewing") != null) {
        isAutoRenewing = jsonElement.get("autoRenewing")
            .getAsBoolean();
      }
      //Base64 decoded string
      byte[] decodedSignature = Base64.decode(signature, Base64.DEFAULT);
      list.add(
          new Purchase(id, skuType, purchaseData, decodedSignature, purchaseTime, purchaseState,
              developerPayload, token, packageName, sku, isAutoRenewing));
    }

    return new PurchasesResult(list, responseCode);
  }

  public Bundle mapArrayListToBundleSkuDetails(List<String> skus) {
    Bundle bundle = new Bundle();
    bundle.putStringArrayList(Utils.GET_SKU_DETAILS_ITEM_LIST, (ArrayList<String>) skus);
    return bundle;
  }

  public SkuDetailsResult mapBundleToHashMapSkuDetails(String skuType, Bundle bundle) {
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

  public SkuDetails parseSkuDetails(String skuType, String skuDetailsData) {
    if (skuDetailsData != "") {
      JsonObject jsonElement = jsonParser.parse(skuDetailsData)
          .getAsJsonObject();

      String sku = jsonElement.get("productId")
          .getAsString();
      String type = jsonElement.get("type")
          .getAsString();
      String price = jsonElement.get("price")
          .getAsString();
      Long priceAmountMicros = jsonElement.get("price_amount_micros")
          .getAsLong();
      String priceCurrencyCode = jsonElement.get("price_currency_code")
          .getAsString();
      String title = jsonElement.get("title")
          .getAsString();
      String description = jsonElement.get("description")
          .getAsString();

      return new SkuDetails(skuType, sku, type, price, priceAmountMicros, priceCurrencyCode, title,
          description);
    } else {
      return new SkuDetails(skuType, "", "", "", 0, "", "", "");
    }
  }

  public SkuDetailsResult mapSkuDetailsFromWS(String skuType, String skuDetailsresponse ,List<String> skus) {
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

  public LaunchBillingFlowResult mapBundleToHashMapGetIntent(Bundle bundle) {

    LaunchBillingFlowResult launchBillingFlowResult =
        new LaunchBillingFlowResult(bundle.get("RESPONSE_CODE"),
            bundle.getParcelable("BUY_INTENT"));
    return launchBillingFlowResult;
  }
}
