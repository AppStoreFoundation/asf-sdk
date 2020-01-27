package com.sdk.appcoins_adyen.api;

import android.os.Parcel;
import com.sdk.appcoins_adyen.exceptions.ModelSerializationException;
import com.sdk.appcoins_adyen.methods.PaymentMethod;
import com.sdk.appcoins_adyen.methods.PaymentMethodsGroup;
import com.sdk.appcoins_adyen.methods.StoredPaymentMethod;
import com.sdk.appcoins_adyen.utils.JsonUtils;
import com.sdk.appcoins_adyen.utils.ModelUtils;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Object that parses and holds the response data from the paymentMethods/ endpoint.
 */
public final class PaymentMethodsApiResponse extends ModelObject {
  public static final Creator<PaymentMethodsApiResponse> CREATOR =
      new Creator<>(PaymentMethodsApiResponse.class);

  private static final String GROUPS = "groups";
  private static final String STORED_PAYMENT_METHODS = "storedPaymentMethods";
  private static final String PAYMENT_METHODS = "paymentMethods";

  public static final Serializer<PaymentMethodsApiResponse> SERIALIZER =
      new Serializer<PaymentMethodsApiResponse>() {
        @Override public JSONObject serialize(PaymentMethodsApiResponse modelObject) {
          final JSONObject jsonObject = new JSONObject();
          try {
            jsonObject.putOpt(GROUPS, ModelUtils.serializeOptList(modelObject.getGroups(),
                PaymentMethodsGroup.SERIALIZER));
            jsonObject.putOpt(STORED_PAYMENT_METHODS,
                ModelUtils.serializeOptList(modelObject.getStoredPaymentMethods(),
                    StoredPaymentMethod.SERIALIZER));
            jsonObject.putOpt(PAYMENT_METHODS,
                ModelUtils.serializeOptList(modelObject.getPaymentMethods(),
                    PaymentMethod.SERIALIZER));
          } catch (JSONException e) {
            throw new ModelSerializationException(PaymentMethodsApiResponse.class, e);
          }
          return jsonObject;
        }

        @Override public PaymentMethodsApiResponse deserialize(JSONObject jsonObject) {
          final PaymentMethodsApiResponse paymentMethodsApiResponse =
              new PaymentMethodsApiResponse();
          paymentMethodsApiResponse.setGroups(
              ModelUtils.deserializeOptList(jsonObject.optJSONArray(GROUPS),
                  PaymentMethodsGroup.SERIALIZER));
          paymentMethodsApiResponse.setStoredPaymentMethods(
              ModelUtils.deserializeOptList(jsonObject.optJSONArray(STORED_PAYMENT_METHODS),
                  StoredPaymentMethod.SERIALIZER));
          paymentMethodsApiResponse.setPaymentMethods(
              ModelUtils.deserializeOptList(jsonObject.optJSONArray(PAYMENT_METHODS),
                  PaymentMethod.SERIALIZER));
          return paymentMethodsApiResponse;
        }
      };

  private List<PaymentMethodsGroup> groups;
  private List<StoredPaymentMethod> storedPaymentMethods;
  private List<PaymentMethod> paymentMethods;

  @Override public void writeToParcel(Parcel dest, int flags) {
    JsonUtils.writeToParcel(dest, SERIALIZER.serialize(this));
  }

  public List<PaymentMethodsGroup> getGroups() {
    return groups;
  }

  public void setGroups(List<PaymentMethodsGroup> groups) {
    this.groups = groups;
  }

  public List<StoredPaymentMethod> getStoredPaymentMethods() {
    return storedPaymentMethods;
  }

  public void setStoredPaymentMethods(List<StoredPaymentMethod> storedPaymentMethods) {
    this.storedPaymentMethods = storedPaymentMethods;
  }

  public List<PaymentMethod> getPaymentMethods() {
    return paymentMethods;
  }

  public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
    this.paymentMethods = paymentMethods;
  }
}

