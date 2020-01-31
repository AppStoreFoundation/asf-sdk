package com.appcoins.sdk.billing.service.adyen;

import com.appcoins.sdk.billing.models.AdyenTransactionResponse;
import com.appcoins.sdk.billing.models.PaymentMethodsResponse;
import com.appcoins.sdk.billing.models.TransactionResponse;
import com.appcoins.sdk.billing.service.RequestResponse;
import java.math.BigDecimal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.appcoins.sdk.billing.utils.ServiceUtils.isSuccess;

public class AdyenMapper {

  public AdyenMapper() {

  }

  public TransactionResponse mapTransactionResponse(RequestResponse requestResponse) {
    JSONObject jsonObject;
    TransactionResponse transactionResponse = new TransactionResponse();
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    String uid;
    String hash;
    String orderReference;
    String status;
    if (isSuccess(code) && response != null) {
      try {
        jsonObject = new JSONObject(response);
        uid = jsonObject.getString("uid");
        hash = jsonObject.getString("hash");
        if (hash.equals("null")) {
          hash = null;
        }
        orderReference = jsonObject.getString("reference");
        status = jsonObject.getString("status");
        transactionResponse =
            new TransactionResponse(uid, hash, orderReference, status, !isSuccess(code));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return transactionResponse;
  }

  public AdyenTransactionResponse mapAdyenTransactionResponse(RequestResponse requestResponse) {
    JSONObject jsonObject;
    AdyenTransactionResponse adyenTransactionResponse = new AdyenTransactionResponse();
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    String uid;
    String hash;
    String orderReference;
    String status;
    String pspReference;
    String resultCode;
    String refusalReason;
    String refusalReasonCode;
    String url = null;
    String paymentData = null;
    if (isSuccess(code) && response != null) {
      try {
        jsonObject = new JSONObject(response);
        uid = jsonObject.getString("uid");
        hash = jsonObject.getString("hash");
        if (hash.equals("null")) {
          hash = null;
        }
        orderReference = jsonObject.getString("reference");
        status = jsonObject.getString("status");
        JSONObject paymentJson = jsonObject.getJSONObject("payment");
        pspReference = paymentJson.optString("pspReference", null);
        resultCode = paymentJson.optString("resultCode", null);
        if (paymentJson.has("action")) {
          JSONObject action = paymentJson.getJSONObject("action");
          url = action.getString("url");
          paymentData = action.getString("paymentData");
        }
        refusalReason = paymentJson.optString("refusalReason", null);
        refusalReasonCode = paymentJson.optString("refusalReasonCode", null);
        adyenTransactionResponse =
            new AdyenTransactionResponse(uid, hash, orderReference, status, pspReference,
                resultCode, url, paymentData, refusalReason, refusalReasonCode, !isSuccess(code));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return adyenTransactionResponse;
  }

  public PaymentMethodsResponse mapPaymentMethodsResponse(RequestResponse requestResponse) {
    JSONObject jsonObject;
    PaymentMethodsResponse paymentMethodsResponse = new PaymentMethodsResponse();
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    BigDecimal value;
    String currency;
    String paymentMethodsApiResponse = "";
    if (isSuccess(code) && response != null) {
      try {
        jsonObject = new JSONObject(response);
        JSONObject priceJsonObject = jsonObject.getJSONObject("price");
        value = new BigDecimal(priceJsonObject.getString("value"));
        currency = priceJsonObject.getString("currency");

        jsonObject = jsonObject.getJSONObject("payment");
        JSONArray array = jsonObject.optJSONArray("paymentMethods");
        JSONObject paymentJSONObject = array.optJSONObject(0);
        if (paymentJSONObject != null) {
          paymentMethodsApiResponse = paymentJSONObject.toString();
        }
        paymentMethodsResponse =
            new PaymentMethodsResponse(value, currency, paymentMethodsApiResponse,
                !isSuccess(code));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return paymentMethodsResponse;
  }
}
