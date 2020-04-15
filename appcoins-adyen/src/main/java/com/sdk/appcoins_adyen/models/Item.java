package com.sdk.appcoins_adyen.models;

import android.os.Parcel;
import com.sdk.appcoins_adyen.api.ModelObject;
import com.sdk.appcoins_adyen.exceptions.ModelSerializationException;
import com.sdk.appcoins_adyen.utils.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

public final class Item extends ModelObject {
  public static final Creator<Item> CREATOR = new Creator<>(Item.class);

  private static final String ID = "id";
  private static final String NAME = "name";

  public static final Serializer<Item> SERIALIZER = new Serializer<Item>() {
    @Override public JSONObject serialize(Item modelObject) {
      final JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.putOpt(ID, modelObject.getId());
        jsonObject.putOpt(NAME, modelObject.getName());
      } catch (JSONException e) {
        throw new ModelSerializationException(Item.class, e);
      }
      return jsonObject;
    }

    @Override public Item deserialize(JSONObject jsonObject) {
      final Item item = new Item();
      item.setId(jsonObject.optString(ID, ""));
      item.setName(jsonObject.optString(NAME, ""));
      return item;
    }
  };

  private String id;
  private String name;

  @Override public void writeToParcel(Parcel dest, int flags) {
    JsonUtils.writeToParcel(dest, SERIALIZER.serialize(this));
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

