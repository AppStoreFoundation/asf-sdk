package com.appcoins.sdk.billing.service.adyen;

import com.appcoins.sdk.billing.mappers.TransactionMapper;
import com.appcoins.sdk.billing.models.billing.AdyenPaymentMethodsModel;
import com.appcoins.sdk.billing.models.billing.AdyenTransactionModel;
import com.appcoins.sdk.billing.models.billing.TransactionModel;
import com.appcoins.sdk.billing.models.payasguest.StoredMethodDetails;
import com.appcoins.sdk.billing.service.RequestResponse;
import java.math.BigDecimal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.appcoins.sdk.billing.utils.ServiceUtils.isSuccess;

public class AdyenMapper {

  private TransactionMapper transactionMapper;

  public AdyenMapper(TransactionMapper transactionMapper) {

    this.transactionMapper = transactionMapper;
  }

  public TransactionModel mapTransactionResponse(RequestResponse requestResponse) {
    return transactionMapper.mapTransactionResponse(requestResponse);
  }

  public AdyenTransactionModel mapAdyenTransactionResponse(RequestResponse requestResponse) {
    JSONObject jsonObject;
    int code = requestResponse.getResponseCode();
    String response = requestResponse.getResponse();
    AdyenTransactionModel adyenTransactionModel =
        AdyenTransactionModel.createErrorAdyenTransactionModel(code);
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
        refusalReasonCode = paymentJson.optString("refusalReasonCode", "-1");
        adyenTransactionModel =
            new AdyenTransactionModel(uid, hash, orderReference, status, pspReference, resultCode,
                url, paymentData, refusalReason, Integer.parseInt(refusalReasonCode),
                !isSuccess(code), code);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return adyenTransactionModel;
  }

  public AdyenPaymentMethodsModel mapPaymentMethodsResponse(RequestResponse requestResponse) {
    JSONObject jsonObject;
    AdyenPaymentMethodsModel paymentMethodsResponse =
        AdyenPaymentMethodsModel.createErrorAdyenPaymentMethodsModel();
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
            new AdyenPaymentMethodsModel(value, currency, paymentMethodsApiResponse,
                storedMethodDetails, !isSuccess(code));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return paymentMethodsResponse;
  }
}
