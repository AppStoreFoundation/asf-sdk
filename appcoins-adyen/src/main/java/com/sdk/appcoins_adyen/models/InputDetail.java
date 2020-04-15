package com.sdk.appcoins_adyen.models;

import android.os.Parcel;
import com.sdk.appcoins_adyen.api.ModelObject;
import com.sdk.appcoins_adyen.exceptions.ModelSerializationException;
import com.sdk.appcoins_adyen.utils.JsonUtils;
import com.sdk.appcoins_adyen.utils.ModelUtils;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public final class InputDetail extends ModelObject {
  public static final Creator<InputDetail> CREATOR = new Creator<>(InputDetail.class);

  private static final String CONFIGURATION = "configuration";
  private static final String DETAILS = "details";
  private static final String ITEM_SEARCH_URL = "itemSearchUrl";
  private static final String ITEMS = "items";
  private static final String KEY = "key";
  private static final String NAME = "name";
  private static final String OPTIONAL = "optional";
  private static final String TYPE = "type";
  private static final String VALIDATION_TYPE = "validationType";
  private static final String VALUE = "value";

  public static final Serializer<InputDetail> SERIALIZER = new Serializer<InputDetail>() {
    @Override public JSONObject serialize(InputDetail modelObject) {
      final JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.putOpt(CONFIGURATION, modelObject.getConfiguration());
        jsonObject.putOpt(DETAILS,
            ModelUtils.serializeOptList(modelObject.getDetails(), InputDetail.SERIALIZER));
        jsonObject.putOpt(ITEM_SEARCH_URL, modelObject.getItemSearchUrl());
        jsonObject.putOpt(ITEMS,
            ModelUtils.serializeOptList(modelObject.getItems(), Item.SERIALIZER));
        jsonObject.putOpt(KEY, modelObject.getKey());
        jsonObject.putOpt(NAME, modelObject.getName());
        jsonObject.putOpt(OPTIONAL, modelObject.isOptional());
        jsonObject.putOpt(TYPE, modelObject.getType());
        jsonObject.putOpt(VALIDATION_TYPE, modelObject.getValidationType());
        jsonObject.putOpt(VALUE, modelObject.getValue());
      } catch (JSONException e) {
        throw new ModelSerializationException(InputDetail.class, e);
      }
      return jsonObject;
    }

    @Override public InputDetail deserialize(JSONObject jsonObject) {
      final InputDetail inputDetail = new InputDetail();
      inputDetail.setConfiguration(jsonObject.optString(CONFIGURATION, ""));
      inputDetail.setDetails(
          ModelUtils.deserializeOptList(jsonObject.optJSONArray(DETAILS), InputDetail.SERIALIZER));
      inputDetail.setItemSearchUrl(jsonObject.optString(ITEM_SEARCH_URL, ""));
      inputDetail.setItems(
          ModelUtils.deserializeOptList(jsonObject.optJSONArray(ITEMS), Item.SERIALIZER));
      inputDetail.setKey(jsonObject.optString(KEY, ""));
      inputDetail.setName(jsonObject.optString(NAME, ""));
      inputDetail.setOptional(jsonObject.optBoolean(OPTIONAL));
      inputDetail.setType(jsonObject.optString(TYPE, ""));
      inputDetail.setValidationType(jsonObject.optString(VALIDATION_TYPE, ""));
      inputDetail.setValue(jsonObject.optString(VALUE, ""));
      return inputDetail;
    }
  };

  private String configuration;
  private List<InputDetail> details;
  private String itemSearchUrl;
  private List<Item> items;
  private String key;
  private String name;
  private boolean optional;
  private String type;
  private String validationType;
  private String value;

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

  public String getItemSearchUrl() {
    return itemSearchUrl;
  }

  public void setItemSearchUrl(String itemSearchUrl) {
    this.itemSearchUrl = itemSearchUrl;
  }

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isOptional() {
    return optional;
  }

  public void setOptional(boolean optional) {
    this.optional = optional;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValidationType() {
    return validationType;
  }

  public void setValidationType(String validationType) {
    this.validationType = validationType;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}