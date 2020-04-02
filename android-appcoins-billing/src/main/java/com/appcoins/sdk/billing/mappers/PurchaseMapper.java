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

  public PurchaseModel map(RequestResponse requestResponse) {
    PurchaseModel purchaseModel = PurchaseModel.createErrorPurchaseModel();
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    if (isSuccess(code) && response != null) {
      try {
        JSONObject jsonObject = new JSONObject(response);
        SkuPurchase skuPurchase = mapSkuPurchase(jsonObject);
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
            SkuPurchase skuPurchase = mapSkuPurchase(jsonObject);
            skuPurchases.add(skuPurchase);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
      purchasesModel = new PurchasesModel(skuPurchases, false);
    } else {
      purchasesModel = PurchasesModel.createErrorPurchasesModel();
    }
    return purchasesModel;
  }

  private SkuPurchase mapSkuPurchase(JSONObject jsonObject) {
    String uid = jsonObject.optString("uid");
    String status = jsonObject.optString("status");

    JSONObject remoteProduct = jsonObject.optJSONObject("product");
    String productName = null;
    if (remoteProduct != null) {
      productName = remoteProduct.optString("name");
    }

    String packageName = null;
    JSONObject packageObject = jsonObject.optJSONObject("package");
    if (packageObject != null) {
      packageName = packageObject.optString("name");
    }

    JSONObject signature = jsonObject.optJSONObject("signature");
    String signatureValue = null;
    JSONObject developerPurchase = null;
    if (signature != null) {
      signatureValue = signature.optString("value");
      developerPurchase = signature.optJSONObject("message");
    }

    return new SkuPurchase(uid, new RemoteProduct(productName), status, packageName,
        new Signature(signatureValue, developerPurchase));
  }
}
