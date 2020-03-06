package com.appcoins.sdk.billing.mappers;

import com.appcoins.sdk.billing.listeners.PurchasesModel;
import com.appcoins.sdk.billing.models.billing.PurchaseModel;
import com.appcoins.sdk.billing.models.billing.RemoteProduct;
import com.appcoins.sdk.billing.models.billing.Signature;
import com.appcoins.sdk.billing.models.billing.SkuPurchase;
import com.appcoins.sdk.billing.service.RequestResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.appcoins.sdk.billing.utils.ServiceUtils.isSuccess;

public class PurchaseMapper {

  public PurchaseMapper() {

  }

  public PurchaseModel map(RequestResponse requestResponse) {
    PurchaseModel purchaseModel = new PurchaseModel();
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    if (isSuccess(code) && response != null) {
      try {
        JSONObject jsonObject = new JSONObject(response);
        String uid = jsonObject.optString("uid");
        JSONObject remoteProduct = jsonObject.optJSONObject("product");
        String productName = null;
        if (remoteProduct != null) {
          productName = remoteProduct.optString("name");
        }
        String status = jsonObject.optString("status");
        String packageName = jsonObject.optString("packageName");
        JSONObject signature = jsonObject.optJSONObject("signature");
        String signatureValue = null;
        JSONObject developerPurchase = null;
        if (signature != null) {
          signatureValue = signature.optString("value");
          developerPurchase = signature.optJSONObject("message");
        }
        SkuPurchase skuPurchase =
            new SkuPurchase(uid, new RemoteProduct(productName), status, packageName,
                new Signature(signatureValue, developerPurchase));
        purchaseModel = new PurchaseModel(skuPurchase, false);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return purchaseModel;
  }

  public PurchasesModel mapList(RequestResponse requestResponse) {
    List<SkuPurchase> skuPurchases = new ArrayList<>();
    PurchasesModel purchasesModel;
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    if (isSuccess(code) && response != null) {
      try {
        JSONArray jsonArray = new JSONObject(response).optJSONArray("items");
        for (int i = 0; i < jsonArray.length(); i++) {
          try {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String uid = jsonObject.optString("uid");
            JSONObject remoteProduct = jsonObject.optJSONObject("product");
            String productName = null;
            if (remoteProduct != null) {
              productName = remoteProduct.optString("name");
            }
            String status = jsonObject.optString("status");
            String packageName = jsonObject.optString("packageName");
            JSONObject signature = jsonObject.optJSONObject("signature");
            String signatureValue = null;
            JSONObject developerPurchase = null;
            if (signature != null) {
              signatureValue = signature.optString("value");
              developerPurchase = signature.optJSONObject("message");
            }
            SkuPurchase skuPurchase =
                new SkuPurchase(uid, new RemoteProduct(productName), status, packageName,
                    new Signature(signatureValue, developerPurchase));
            skuPurchases.add(skuPurchase);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
      purchasesModel = new PurchasesModel(skuPurchases, false);
    } else {
      purchasesModel = new PurchasesModel();
    }
    return purchasesModel;
  }
}
