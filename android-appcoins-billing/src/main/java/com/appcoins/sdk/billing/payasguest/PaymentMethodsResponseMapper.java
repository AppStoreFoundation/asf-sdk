package com.appcoins.sdk.billing.payasguest;

import com.appcoins.sdk.billing.service.RequestResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.appcoins.sdk.billing.utils.ServiceUtils.isSuccess;

class PaymentMethodsResponseMapper {

  public PaymentMethodsResponseMapper() {

  }

  public PaymentMethodsModel map(RequestResponse requestResponse) {
    JSONObject jsonObject;
    PaymentMethodsModel paymentMethodsModel = new PaymentMethodsModel();
    String response = requestResponse.getResponse();
    List<PaymentMethod> paymentMethods = new ArrayList<>();
    int code = requestResponse.getResponseCode();
    if (isSuccess(code) && response != null) {
      try {
        jsonObject = new JSONObject(response);
        JSONArray array = jsonObject.getJSONArray("items");
        for (int i = 0; i < array.length(); i++) {
          JSONObject object = array.getJSONObject(i);
          String name = object.getString("name");
          String availability = object.getString("status");
          PaymentMethod paymentMethod =
              new PaymentMethod(name, availability.equalsIgnoreCase("AVAILABLE"));
          paymentMethods.add(paymentMethod);
        }
        paymentMethodsModel = new PaymentMethodsModel(paymentMethods, false);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return paymentMethodsModel;
  }
}
