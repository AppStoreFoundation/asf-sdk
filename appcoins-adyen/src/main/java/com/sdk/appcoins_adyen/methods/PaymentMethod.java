package com.sdk.appcoins_adyen.methods;

import android.os.Parcel;
import com.sdk.appcoins_adyen.api.ModelObject;
import com.sdk.appcoins_adyen.exceptions.ModelSerializationException;
import com.sdk.appcoins_adyen.models.Group;
import com.sdk.appcoins_adyen.models.InputDetail;
import com.sdk.appcoins_adyen.utils.JsonUtils;
import com.sdk.appcoins_adyen.utils.ModelUtils;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentMethod extends ModelObject {
  public static final Creator<PaymentMethod> CREATOR = new Creator<>(PaymentMethod.class);

  private static final String CONFIGURATION = "configuration";
  private static final String DETAILS = "details";
  private static final String GROUP = "group";
  private static final String NAME = "name";
  private static final String BRANDS = "brands";
  private static final String PAYMENT_METHOD_DATA = "paymentMethodData";
  private static final String SUPPORTS_RECURRING = "supportsRecurring";
  private static final String TYPE = "type";

  public static final Serializer<PaymentMethod> SERIALIZER = new Serializer<PaymentMethod>() {

    @Override public JSONObject serialize(PaymentMethod modelObject) {
      final JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.putOpt(CONFIGURATION, modelObject.getConfiguration());
        jsonObject.putOpt(DETAILS,
            ModelUtils.serializeOptList(modelObject.getDetails(), InputDetail.SERIALIZER));
        jsonObject.putOpt(GROUP, ModelUtils.serializeOpt(modelObject.getGroup(), Group.SERIALIZER));
        jsonObject.putOpt(NAME, modelObject.getName());
        jsonObject.putOpt(BRANDS, JsonUtils.serializeOptStringList(modelObject.getBrands()));
        jsonObject.putOpt(PAYMENT_METHOD_DATA, modelObject.getPaymentMethodData());
        jsonObject.putOpt(SUPPORTS_RECURRING, modelObject.getSupportsRecurring());
        jsonObject.putOpt(TYPE, modelObject.getType());
      } catch (JSONException e) {
        throw new ModelSerializationException(PaymentMethod.class, e);
      }
      return jsonObject;
    }

    @Override public PaymentMethod deserialize(JSONObject jsonObject) {
      final PaymentMethod paymentMethod = new PaymentMethod();
      paymentMethod.setConfiguration(jsonObject.optString(CONFIGURATION, ""));
      paymentMethod.setDetails(
          ModelUtils.deserializeOptList(jsonObject.optJSONArray(DETAILS), InputDetail.SERIALIZER));
      paymentMethod.setGroup(
          ModelUtils.deserializeOpt(jsonObject.optJSONObject(GROUP), Group.SERIALIZER));
      paymentMethod.setName(jsonObject.optString(NAME, ""));
      paymentMethod.setBrands(JsonUtils.parseOptStringList(jsonObject.optJSONArray(BRANDS)));
      paymentMethod.setPaymentMethodData(jsonObject.optString(PAYMENT_METHOD_DATA, ""));
      paymentMethod.setSupportsRecurring(jsonObject.optBoolean(SUPPORTS_RECURRING, false));
      paymentMethod.setType(jsonObject.optString(TYPE, ""));
      return paymentMethod;
    }
  };

  private String configuration;
  private List<InputDetail> details;
  private Group group;
  private String name;
  private List<String> brands;
  private String paymentMethodData;
  private boolean supportsRecurring;
  private String type;

  @Override public void writeToParcel(Parcel dest, int flags) {
    JsonUtils.writeToParcel(dest, SERIALIZER.serialize(this));
  }

  public String getConfiguration() {
    return configuration;
  }

  public void setConfiguration(String configuration) {
    this.configuration = configuration;
  }

  public List<InputDetail> getDetails() {
    return details;
  }

  public void setDetails(List<InputDetail> details) {
    this.details = details;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPaymentMethodData() {
    return paymentMethodData;
  }

  public void setPaymentMethodData(String paymentMethodData) {
    this.paymentMethodData = paymentMethodData;
  }

  public boolean getSupportsRecurring() {
    return supportsRecurring;
  }

  public void setSupportsRecurring(boolean supportsRecurring) {
    this.supportsRecurring = supportsRecurring;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<String> getBrands() {
    return brands;
  }

  public void setBrands(List<String> brands) {
    this.brands = brands;
  }
}

