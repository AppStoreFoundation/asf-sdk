package com.appcoins.sdk.billing.service.adyen;

import android.util.Log;
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

  TransactionResponse mapTransactionResponse(RequestResponse requestResponse) {
    JSONObject jsonObject;
    TransactionResponse transactionResponse = new TransactionResponse();
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    String uid;
    String hash;
    String orderReference;
    String status;
    if (isSuccess(code) && response != null) {
      Log.d("TAG123", "TransactionResponse: ");
      try {
        jsonObject = new JSONObject(response);
        uid = jsonObject.getString("uid");
        hash = jsonObject.getString("hash");
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

  AdyenTransactionResponse mapAdyenTransactionResponse(RequestResponse requestResponse) {
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
    String refusalReason = null;
    String refusalReasonCode = null;
    String url = null;
    String paymentData = null;
    if (isSuccess(code) && response != null) {
      Log.d("TAG123", "AdyenTransactionResponse: " + response);
      try {
        jsonObject = new JSONObject(response);
        uid = jsonObject.getString("uid");
        hash = jsonObject.getString("hash");
        orderReference = jsonObject.getString("reference");
        status = jsonObject.getString("status");
        JSONObject paymentJson = new JSONObject(jsonObject.getString("payment"));
        pspReference = paymentJson.getString("pspReference");
        resultCode = paymentJson.getString("resultCode");
        if (paymentJson.has("action")) {
          JSONObject action = new JSONObject(paymentJson.getString("action"));
          url = action.getString("url");
          paymentData = action.getString("paymentData");
        }
        if (paymentJson.has("refusalReason")) {
          refusalReason = paymentJson.getString("refusalReason");
        }
        if (paymentJson.has("refusalReasonCode")) {
          refusalReasonCode = paymentJson.getString("refusalReasonCode");
        }
        adyenTransactionResponse =
            new AdyenTransactionResponse(uid, hash, orderReference, status, pspReference,
                resultCode, url, paymentData, refusalReason, refusalReasonCode, !isSuccess(code));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return adyenTransactionResponse;
  }

  PaymentMethodsResponse mapPaymentMethodsResponse(RequestResponse requestResponse) {
    JSONObject jsonObject;
    PaymentMethodsResponse paymentMethodsResponse = new PaymentMethodsResponse();
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    BigDecimal value;
    String currency;
    String paymentMethodsApiResponse;
    if (isSuccess(code) && response != null) {
      Log.d("TAG123", "PaymentMethodsResponse: " + response);
      try {
        jsonObject = new JSONObject(response);
        JSONObject priceJsonObject = new JSONObject(jsonObject.getString("price"));
        value = new BigDecimal(priceJsonObject.getString("value"));
        currency = priceJsonObject.getString("currency");

        jsonObject = new JSONObject(jsonObject.getString("payment"));
        JSONArray array = jsonObject.optJSONArray("paymentMethods");
        paymentMethodsApiResponse = array.getJSONObject(0)
            .toString();
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
