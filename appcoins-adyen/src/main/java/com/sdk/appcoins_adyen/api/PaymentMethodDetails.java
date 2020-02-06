package com.sdk.appcoins_adyen.api;

import android.text.TextUtils;
import com.sdk.appcoins_adyen.exceptions.CheckoutException;
import com.sdk.appcoins_adyen.methods.GenericPaymentMethod;
import org.json.JSONObject;

public abstract class PaymentMethodDetails extends ModelObject {

  public static final String TYPE = "type";

  public static final Serializer<PaymentMethodDetails> SERIALIZER =
      new Serializer<PaymentMethodDetails>() {

        @Override public JSONObject serialize(PaymentMethodDetails modelObject) {
          final String paymentMethodType = modelObject.getType();
          if (TextUtils.isEmpty(paymentMethodType)) {
            throw new CheckoutException("PaymentMethod type not found");
          }
          //noinspection unchecked
          final Serializer<PaymentMethodDetails> serializer =
              (Serializer<PaymentMethodDetails>) getChildSerializer(paymentMethodType);
          return serializer.serialize(modelObject);
        }

        @Override public PaymentMethodDetails deserialize(JSONObject jsonObject) {
          final String actionType = jsonObject.optString(TYPE, null);
          if (TextUtils.isEmpty(actionType)) {
            throw new CheckoutException("PaymentMethod type not found");
          }
          //noinspection unchecked
          final Serializer<PaymentMethodDetails> serializer =
              (Serializer<PaymentMethodDetails>) getChildSerializer(actionType);
          return serializer.deserialize(jsonObject);
        }
      };

  private String type;

  static Serializer<? extends PaymentMethodDetails> getChildSerializer(String paymentMethodType) {
    if (CardPaymentMethod.PAYMENT_METHOD_TYPE.equals(paymentMethodType)) {
      return CardPaymentMethod.SERIALIZER;
    }
    return GenericPaymentMethod.SERIALIZER;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}

