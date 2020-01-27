package com.sdk.appcoins_adyen.methods;

import android.os.Parcel;
import com.sdk.appcoins_adyen.api.ModelObject;
import com.sdk.appcoins_adyen.exceptions.ModelSerializationException;
import com.sdk.appcoins_adyen.utils.JsonUtils;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public final class PaymentMethodsGroup extends ModelObject {
  public static final Creator<PaymentMethodsGroup> CREATOR =
      new Creator<>(PaymentMethodsGroup.class);

  private static final String GROUP_TYPE = "groupType";
  private static final String NAME = "name";
  private static final String TYPES = "types";

  public static final Serializer<PaymentMethodsGroup> SERIALIZER =
      new Serializer<PaymentMethodsGroup>() {
        @Override public JSONObject serialize(PaymentMethodsGroup modelObject) {
          final JSONObject jsonObject = new JSONObject();
          try {
            jsonObject.putOpt(GROUP_TYPE, modelObject.getGroupType());
            jsonObject.putOpt(NAME, modelObject.getName());
            jsonObject.putOpt(TYPES, JsonUtils.serializeOptStringList(modelObject.getTypes()));
          } catch (JSONException e) {
            throw new ModelSerializationException(PaymentMethodsGroup.class, e);
          }
          return jsonObject;
        }

        @Override public PaymentMethodsGroup deserialize(JSONObject jsonObject) {
          final PaymentMethodsGroup paymentMethodsGroup = new PaymentMethodsGroup();
          paymentMethodsGroup.setGroupType(jsonObject.optString(GROUP_TYPE, ""));
          paymentMethodsGroup.setName(jsonObject.optString(NAME, ""));
          paymentMethodsGroup.setTypes(
              JsonUtils.parseOptStringList(jsonObject.optJSONArray(TYPES)));
          return paymentMethodsGroup;
        }
      };

  private String groupType;
  private String name;
  private List<String> types;

  @Override public void writeToParcel(Parcel dest, int flags) {
    JsonUtils.writeToParcel(dest, SERIALIZER.serialize(this));
  }

  public String getGroupType() {
    return groupType;
  }

  public void setGroupType(String groupType) {
    this.groupType = groupType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getTypes() {
    return types;
  }

  public void setTypes(List<String> types) {
    this.types = types;
  }
}
