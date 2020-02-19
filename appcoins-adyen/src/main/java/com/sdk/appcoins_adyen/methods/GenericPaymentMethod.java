package com.sdk.appcoins_adyen.methods;

import android.os.Parcel;
import com.sdk.appcoins_adyen.api.ModelObject;
import com.sdk.appcoins_adyen.api.PaymentMethodDetails;
import com.sdk.appcoins_adyen.exceptions.ModelSerializationException;
import com.sdk.appcoins_adyen.utils.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class GenericPaymentMethod extends PaymentMethodDetails {
  public static final ModelObject.Creator<GenericPaymentMethod> CREATOR =
      new ModelObject.Creator<>(GenericPaymentMethod.class);

  public static final ModelObject.Serializer<GenericPaymentMethod> SERIALIZER =
      new ModelObject.Serializer<GenericPaymentMethod>() {

        @Override public JSONObject serialize(GenericPaymentMethod modelObject) {
          final JSONObject jsonObject = new JSONObject();
          try {
            // getting parameters from parent class
            jsonObject.putOpt(PaymentMethodDetails.TYPE, modelObject.getType());
          } catch (JSONException e) {
            throw new ModelSerializationException(GenericPaymentMethod.class, e);
          }
          return jsonObject;
        }

        @Override public GenericPaymentMethod deserialize(JSONObject jsonObject) {
          return new GenericPaymentMethod(jsonObject.optString(PaymentMethodDetails.TYPE, null));
        }
      };

  public GenericPaymentMethod(String paymentType) {
    setType(paymentType);
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    JsonUtils.writeToParcel(dest, SERIALIZER.serialize(this));
  }
}
