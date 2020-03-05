package com.appcoins.sdk.billing.mappers;

import com.appcoins.sdk.billing.models.billing.PurchaseModel;
import com.appcoins.sdk.billing.models.billing.RemoteProduct;
import com.appcoins.sdk.billing.models.billing.Signature;
import com.appcoins.sdk.billing.models.billing.SkuPurchase;
import com.appcoins.sdk.billing.service.RequestResponse;
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
}
