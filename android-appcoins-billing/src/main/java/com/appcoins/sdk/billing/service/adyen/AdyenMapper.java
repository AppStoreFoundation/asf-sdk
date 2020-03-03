package com.appcoins.sdk.billing.service.adyen;

import com.appcoins.sdk.billing.models.AdyenTransactionModel;
import com.appcoins.sdk.billing.models.PaymentMethodsModel;
import com.appcoins.sdk.billing.models.StoredMethodDetails;
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

  public AdyenTransactionModel mapAdyenTransactionResponse(RequestResponse requestResponse) {
    JSONObject jsonObject;
    AdyenTransactionModel adyenTransactionModel = new AdyenTransactionModel();
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
        adyenTransactionModel =
            new AdyenTransactionModel(uid, hash, orderReference, status, pspReference, resultCode,
                url, paymentData, refusalReason, refusalReasonCode, !isSuccess(code));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return adyenTransactionModel;
  }

  public PaymentMethodsModel mapPaymentMethodsResponse(RequestResponse requestResponse) {
    JSONObject jsonObject;
    PaymentMethodsModel paymentMethodsResponse = new PaymentMethodsModel();
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    BigDecimal value;
    String currency;
    String paymentMethodsApiResponse = "";
    StoredMethodDetails storedMethodDetails = null;
    if (isSuccess(code) && response != null) {
      try {
        jsonObject = new JSONObject(response);
        JSONObject priceJsonObject = jsonObject.getJSONObject("price");
        value = new BigDecimal(priceJsonObject.getString("value"));
        currency = priceJsonObject.getString("currency");

        jsonObject = jsonObject.getJSONObject("payment");
        JSONArray paymentsArray = jsonObject.optJSONArray("paymentMethods");
        JSONArray storedArray = jsonObject.optJSONArray("storedPaymentMethods");

        JSONObject paymentJSONObject = paymentsArray.optJSONObject(0);
        if (paymentJSONObject != null) {
          paymentMethodsApiResponse = paymentJSONObject.toString();
        }

        if (storedArray != null) {
          JSONObject storedJSONObject = storedArray.optJSONObject(0);
          if (storedJSONObject != null) {
            String cardNumber = storedJSONObject.getString("lastFour");
            int expiryMonth = storedJSONObject.getInt("expiryMonth");
            int expiryYear = storedJSONObject.getInt("expiryYear");
            String paymentId = storedJSONObject.getString("id");
            String type = storedJSONObject.getString("type");
            storedMethodDetails =
                new StoredMethodDetails(cardNumber, expiryMonth, expiryYear, paymentId, type);
          }
        }
        paymentMethodsResponse =
            new PaymentMethodsModel(value, currency, paymentMethodsApiResponse, storedMethodDetails,
                !isSuccess(code));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return paymentMethodsResponse;
  }
}
