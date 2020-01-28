package com.appcoins.sdk.billing.service;

import com.appcoins.sdk.billing.listeners.AdyenLoadPaymentInfoListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdyenRepository {

  private Service bdsService;

  public AdyenRepository(Service bdsService) {

    this.bdsService = bdsService;
  }

  public void loadPaymentInfo(String method, String value, String currency, String walletAddress,
      final AdyenLoadPaymentInfoListener listener) {
    Map<String, String> queries = new LinkedHashMap<>();
    queries.put("wallet.address", walletAddress);
    queries.put("price.value", value);
    queries.put("price.currency", currency);
    queries.put("method", method);
    ServiceResponseListener serviceResponseListener = createLoadPaymentInfoListener(listener);

    bdsService.makeRequest("payment-methods", "GET", new ArrayList<String>(), queries, null,
        serviceResponseListener);
  }

  public void makePayment(String adyenPaymentMethod, boolean shouldStorePaymentMethod,
      String returnUrl, String value, String currency, String reference, String paymentType,
      String walletAddress, String origin, String packageName, String metadata, String sku,
      String callbackUrl, String transactionType, String developerWallet, String storeWallet,
      String oemWallet, String userWallet, final AdyenLoadPaymentInfoListener listener) {
    Map<String, String> queries = new HashMap<>();
    queries.put("wallet.address", walletAddress);

    String body =
        buildMockedBody(adyenPaymentMethod, shouldStorePaymentMethod, returnUrl, value, currency,
            reference, paymentType, origin, packageName, metadata, sku, callbackUrl,
            transactionType, developerWallet, storeWallet, oemWallet, userWallet);
    ServiceResponseListener serviceResponseListener = createMakePaymentListener(listener);

    bdsService.makeRequest("transactions", "POST", new ArrayList<String>(), queries, body,
        serviceResponseListener);
  }

  private String buildMockedBody(String adyenPaymentMethod, boolean shouldStorePaymentMethod,
      String returnUrl, String value, String currency, String reference, String paymentType,
      String origin, String packageName, String metadata, String sku, String callbackUrl,
      String transactionType, String developerWallet, String storeWallet, String oemWallet,
      String userWallet) {
    String body = "{\n";
    body += "\t\"payment.method\":"
        + adyenPaymentMethod
        + ","
        + "\n\t\"price.currency\":"
        + currency
        + ","
        + "\n\t\"wallets.developer\":"
        + developerWallet
        + ","
        + "\n\t\"domain\":"
        + packageName
        + ","
        + "\n\t\"metadata\":"
        + metadata
        + ","
        + "\n\t\"method\":"
        + paymentType
        + ","
        + "\n\t\"wallets.oem\":"
        + oemWallet
        + ","
        + "\n\t\"origin\":"
        + origin
        + ","
        + "\n\t\"reference\":"
        + reference
        + ","
        + "\n\t\"payment.return_url\":"
        + returnUrl
        + ","
        + "\n\t\"payment.store_method\":"
        + shouldStorePaymentMethod
        + ","
        + "\n\t\"product\":"
        + sku
        + ","
        + "\n\t\"wallets.store\":"
        + storeWallet
        + ","
        + "\n\t\"type\":"
        + transactionType
        + ","
        + "\n\t\"wallets.user\":"
        + userWallet
        + ","
        + "\n\t\"price.value\":"
        + value;
    if (callbackUrl != null) {
      body += "," + "\n\t\"callback_url\":" + callbackUrl;
    }
    body += "\n}";
    return body;
  }

  private ServiceResponseListener createLoadPaymentInfoListener(
      final AdyenLoadPaymentInfoListener listener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        JSONObject jsonObject = new JSONObject();
        int code = requestResponse.getResponseCode();
        String apiResponse = requestResponse.getResponse();
        if (code == 200 && apiResponse != null) {
          try {
            //Only for paypal as we don't need this object for the CC as it would only be used
            // for the layout which we won't use
            jsonObject = new JSONObject(apiResponse);
            jsonObject = new JSONObject(jsonObject.getString("payment"));
            JSONArray array = jsonObject.optJSONArray("paymentMethods");
            jsonObject = array.getJSONObject(0);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
        listener.onResponse(code, jsonObject.toString(), requestResponse.getException());
      }
    };
  }

  private ServiceResponseListener createMakePaymentListener(
      final AdyenLoadPaymentInfoListener listener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        JSONObject jsonObject;
        int code = requestResponse.getResponseCode();
        String apiResponse = requestResponse.getResponse();
        if (code == 200 && apiResponse != null) {
          try {
            jsonObject = new JSONObject(apiResponse);
            jsonObject = new JSONObject(jsonObject.getString("payment"));
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
        //TODO map the response to the needed object
        listener.onResponse(code, apiResponse, requestResponse.getException());
      }
    };
  }
}
