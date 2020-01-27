package com.sdk.appcoins_adyen.api;

import android.text.TextUtils;
import com.sdk.appcoins_adyen.exceptions.CheckoutException;
import org.json.JSONObject;

public abstract class Action extends ModelObject {

  static final String TYPE = "type";
  public static final Serializer<Action> SERIALIZER = new Serializer<Action>() {

    @Override public JSONObject serialize(Action modelObject) {
      final String actionType = modelObject.getType();
      if (TextUtils.isEmpty(actionType)) {
        throw new CheckoutException("Action type not found");
      }
      //noinspection unchecked
      final Serializer<Action> serializer = (Serializer<Action>) getChildSerializer(actionType);
      return serializer.serialize(modelObject);
    }

    @Override public Action deserialize(JSONObject jsonObject) {
      final String actionType = jsonObject.optString(TYPE);
      if (TextUtils.isEmpty(actionType)) {
        throw new CheckoutException("Action type not found");
      }
      //noinspection unchecked
      final Serializer<Action> serializer = (Serializer<Action>) getChildSerializer(actionType);
      return serializer.deserialize(jsonObject);
    }
  };
  static final String PAYMENT_DATA = "paymentData";
  static final String PAYMENT_METHOD_TYPE = "paymentMethodType";
  private String type;
  private String paymentData;
  private String paymentMethodType;

  private static Serializer<? extends Action> getChildSerializer(String actionType) {
    //Only copied the ones that are being used for now in the sdk
    if (RedirectAction.ACTION_TYPE.equals(actionType)) {
      return RedirectAction.SERIALIZER;
    }
    throw new CheckoutException("Action type not found - " + actionType);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  String getPaymentData() {
    return paymentData;
  }

  void setPaymentData(String paymentData) {
    this.paymentData = paymentData;
  }

  String getPaymentMethodType() {
    return paymentMethodType;
  }

  void setPaymentMethodType(String paymentMethodType) {
    this.paymentMethodType = paymentMethodType;
  }
}
