package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.os.Bundle;
import com.appcoins.sdk.billing.Purchase;
import com.appcoins.sdk.billing.PurchasesResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;

class AndroidBillingMapper {
  private final Gson gson;

  public AndroidBillingMapper(Gson gson) {
    this.gson = gson;
  }

  //hacking check if this map is correct
  public PurchasesResult map(Bundle bundle, String skuType) {
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

      JsonObject jsonElement = new JsonParser().parse(purchaseData)
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
      String developerPayload = jsonElement.get("developerPayload")
          .getAsString();
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

      list.add(new Purchase(id, skuType, purchaseData, signature, purchaseTime, purchaseState,
          developerPayload, token, packageName, sku, isAutoRenewing));
    }

    return new PurchasesResult(list, responseCode);
  }
}
