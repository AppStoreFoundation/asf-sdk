package com.sdk.appcoins_adyen.models;

import android.os.Parcel;
import com.sdk.appcoins_adyen.api.ModelObject;
import com.sdk.appcoins_adyen.exceptions.ModelSerializationException;
import com.sdk.appcoins_adyen.utils.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

public final class Group extends ModelObject {
  public static final Creator<Group> CREATOR = new Creator<>(Group.class);

  private static final String NAME = "name";
  private static final String PAYMENT_METHOD_DATA = "paymentMethodData";
  private static final String TYPE = "type";

  public static final Serializer<Group> SERIALIZER = new Serializer<Group>() {
    @Override public JSONObject serialize(Group modelObject) {
      final JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.putOpt(NAME, modelObject.getName());
        jsonObject.putOpt(PAYMENT_METHOD_DATA, modelObject.getPaymentMethodData());
        jsonObject.putOpt(TYPE, modelObject.getType());
      } catch (JSONException e) {
        throw new ModelSerializationException(Group.class, e);
      }
      return jsonObject;
    }

    @Override public Group deserialize(JSONObject jsonObject) {
      final Group group = new Group();
      group.setName(jsonObject.optString(NAME, ""));
      group.setPaymentMethodData(jsonObject.optString(PAYMENT_METHOD_DATA, ""));
      group.setType(jsonObject.optString(TYPE, ""));
      return group;
    }
  };

  private String name;
  private String paymentMethodData;
  private String type;

  @Override public void writeToParcel(Parcel dest, int flags) {
    JsonUtils.writeToParcel(dest, SERIALIZER.serialize(this));
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
