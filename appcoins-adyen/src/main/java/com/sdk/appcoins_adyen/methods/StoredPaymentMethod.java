package com.sdk.appcoins_adyen.methods;

import android.os.Parcel;
import com.sdk.appcoins_adyen.api.ModelObject;
import com.sdk.appcoins_adyen.exceptions.ModelSerializationException;
import com.sdk.appcoins_adyen.utils.JsonUtils;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoredPaymentMethod extends PaymentMethod {
  public static final ModelObject.Creator<StoredPaymentMethod> CREATOR =
      new ModelObject.Creator<>(StoredPaymentMethod.class);

  private static final String BRAND = "brand";
  private static final String EXPIRY_MONTH = "expiryMonth";
  private static final String EXPIRY_YEAR = "expiryYear";
  private static final String HOLDER_NAME = "holderName";
  private static final String ID = "id";
  private static final String LAST_FOUR = "lastFour";
  private static final String SHOPPER_EMAIL = "shopperEmail";
  private static final String SUPPORTED_SHOPPER_INTERACTIONS = "supportedShopperInteractions";
  public static final Serializer<StoredPaymentMethod> SERIALIZER =
      new Serializer<StoredPaymentMethod>() {
        @Override public JSONObject serialize(StoredPaymentMethod modelObject) {
          // Get parameters from parent class
          final JSONObject jsonObject = PaymentMethod.SERIALIZER.serialize(modelObject);
          try {
            jsonObject.putOpt(BRAND, modelObject.getBrand());
            jsonObject.putOpt(EXPIRY_MONTH, modelObject.getExpiryMonth());
            jsonObject.putOpt(EXPIRY_YEAR, modelObject.getExpiryYear());
            jsonObject.putOpt(HOLDER_NAME, modelObject.getHolderName());
            jsonObject.putOpt(ID, modelObject.getId());
            jsonObject.putOpt(LAST_FOUR, modelObject.getLastFour());
            jsonObject.putOpt(SHOPPER_EMAIL, modelObject.getShopperEmail());
            jsonObject.putOpt(SUPPORTED_SHOPPER_INTERACTIONS,
                new JSONArray(modelObject.getSupportedShopperInteractions()));
          } catch (JSONException e) {
            throw new ModelSerializationException(StoredPaymentMethod.class, e);
          }
          return jsonObject;
        }

        @Override public StoredPaymentMethod deserialize(JSONObject jsonObject) {
          final StoredPaymentMethod storedPaymentMethod = new StoredPaymentMethod();

          // getting parameters from parent class
          final PaymentMethod paymentMethod = PaymentMethod.SERIALIZER.deserialize(jsonObject);
          storedPaymentMethod.setConfiguration(paymentMethod.getConfiguration());
          storedPaymentMethod.setDetails(paymentMethod.getDetails());
          storedPaymentMethod.setGroup(paymentMethod.getGroup());
          storedPaymentMethod.setName(paymentMethod.getName());
          storedPaymentMethod.setPaymentMethodData(paymentMethod.getPaymentMethodData());
          storedPaymentMethod.setSupportsRecurring(paymentMethod.getSupportsRecurring());
          storedPaymentMethod.setType(paymentMethod.getType());

          storedPaymentMethod.setBrand(jsonObject.optString(BRAND));
          storedPaymentMethod.setExpiryMonth(jsonObject.optString(EXPIRY_MONTH));
          storedPaymentMethod.setExpiryYear(jsonObject.optString(EXPIRY_YEAR));
          storedPaymentMethod.setHolderName(jsonObject.optString(HOLDER_NAME));
          storedPaymentMethod.setId(jsonObject.optString(ID));
          storedPaymentMethod.setLastFour(jsonObject.optString(LAST_FOUR));
          storedPaymentMethod.setShopperEmail(jsonObject.optString(SHOPPER_EMAIL));

          final List<String> supportedShopperInteractions =
              JsonUtils.parseOptStringList(jsonObject.optJSONArray(SUPPORTED_SHOPPER_INTERACTIONS));

          if (supportedShopperInteractions != null) {
            storedPaymentMethod.setSupportedShopperInteractions(supportedShopperInteractions);
          }

          return storedPaymentMethod;
        }
      };
  private static final String ECOMMERCE = "Ecommerce";
  private String brand;
  private String expiryMonth;
  private String expiryYear;
  private String holderName;
  private String id;
  private String lastFour;
  private String shopperEmail;
  private List<String> supportedShopperInteractions = Collections.emptyList();

  @Override public void writeToParcel(Parcel dest, int flags) {
    JsonUtils.writeToParcel(dest, SERIALIZER.serialize(this));
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getExpiryMonth() {
    return expiryMonth;
  }

  public void setExpiryMonth(String expiryMonth) {
    this.expiryMonth = expiryMonth;
  }

  public String getExpiryYear() {
    return expiryYear;
  }

  public void setExpiryYear(String expiryYear) {
    this.expiryYear = expiryYear;
  }

  public String getLastFour() {
    return lastFour;
  }

  public void setLastFour(String lastFour) {
    this.lastFour = lastFour;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public List<String> getSupportedShopperInteractions() {
    return supportedShopperInteractions;
  }

  public void setSupportedShopperInteractions(List<String> supportedShopperInteractions) {
    this.supportedShopperInteractions = supportedShopperInteractions;
  }

  public String getHolderName() {
    return holderName;
  }

  public void setHolderName(String holderName) {
    this.holderName = holderName;
  }

  public String getShopperEmail() {
    return shopperEmail;
  }

  public void setShopperEmail(String shopperEmail) {
    this.shopperEmail = shopperEmail;
  }

  public boolean isEcommerce() {
    return supportedShopperInteractions.contains(ECOMMERCE);
  }
}
