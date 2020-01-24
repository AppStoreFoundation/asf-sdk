package com.appcoins.sdk.billing.service;

import com.adyen.checkout.base.model.PaymentMethodsApiResponse;
import com.appcoins.sdk.billing.listeners.AdyenLoadPaymentInfoListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
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
    ServiceResponseListener serviceResponseListener = new ServiceResponseListener() {
      @Override public void onResponseReceived(int code, String apiResponse, Exception exception) {
        PaymentMethodsApiResponse paymentMethodsApiResponse = null;
        JSONObject jsonObject = new JSONObject();
        if (code == 200 && apiResponse != null) {
          try {
            jsonObject = new JSONObject(apiResponse);
            jsonObject = new JSONObject(jsonObject.getString("payment"));
          } catch (JSONException e) {
            e.printStackTrace();
          }
          paymentMethodsApiResponse = PaymentMethodsApiResponse.SERIALIZER.deserialize(jsonObject);
        }
        listener.onResponse(code, paymentMethodsApiResponse, exception);
      }
    };
    bdsService.makeRequest("payment-methods", "GET", new ArrayList<String>(), queries, null,
        serviceResponseListener);
  }
}
