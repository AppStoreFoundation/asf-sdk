package com.sdk.appcoins_adyen.api;

import android.os.Parcel;
import com.sdk.appcoins_adyen.exceptions.ModelSerializationException;
import com.sdk.appcoins_adyen.methods.PaymentMethodTypes;
import com.sdk.appcoins_adyen.utils.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

public final class CardPaymentMethod extends PaymentMethodDetails {

  public static final ModelObject.Creator<CardPaymentMethod> CREATOR =
      new ModelObject.Creator<>(CardPaymentMethod.class);

  public static final String PAYMENT_METHOD_TYPE = PaymentMethodTypes.SCHEME;

  private static final String ENCRYPTED_CARD_NUMBER = "encryptedCardNumber";
  private static final String ENCRYPTED_EXPIRY_MONTH = "encryptedExpiryMonth";
  private static final String ENCRYPTED_EXPIRY_YEAR = "encryptedExpiryYear";
  private static final String ENCRYPTED_SECURITY_CODE = "encryptedSecurityCode";
  private static final String HOLDER_NAME = "holderName";
  private static final String STORED_PAYMENT_METHOD_ID = "storedPaymentMethodId";

  public static final ModelObject.Serializer<CardPaymentMethod> SERIALIZER =
      new ModelObject.Serializer<CardPaymentMethod>() {

        @Override public JSONObject serialize(CardPaymentMethod modelObject) {
          final JSONObject jsonObject = new JSONObject();
          try {
            // getting parameters from parent class
            jsonObject.putOpt(PaymentMethodDetails.TYPE, modelObject.getType());

            jsonObject.putOpt(ENCRYPTED_CARD_NUMBER, modelObject.getEncryptedCardNumber());
            jsonObject.putOpt(ENCRYPTED_EXPIRY_MONTH, modelObject.getEncryptedExpiryMonth());
            jsonObject.putOpt(ENCRYPTED_EXPIRY_YEAR, modelObject.getEncryptedExpiryYear());
            jsonObject.putOpt(ENCRYPTED_SECURITY_CODE, modelObject.getEncryptedSecurityCode());
            jsonObject.putOpt(STORED_PAYMENT_METHOD_ID, modelObject.getStoredPaymentMethodId());
            jsonObject.putOpt(HOLDER_NAME, modelObject.getHolderName());
          } catch (JSONException e) {
            throw new ModelSerializationException(CardPaymentMethod.class, e);
          }
          return jsonObject;
        }

        @Override public CardPaymentMethod deserialize(JSONObject jsonObject) {
          final CardPaymentMethod cardPaymentMethod = new CardPaymentMethod();

          // getting parameters from parent class
          cardPaymentMethod.setType(jsonObject.optString(PaymentMethodDetails.TYPE, ""));

          cardPaymentMethod.setEncryptedCardNumber(jsonObject.optString(ENCRYPTED_CARD_NUMBER, ""));
          cardPaymentMethod.setEncryptedExpiryMonth(
              jsonObject.optString(ENCRYPTED_EXPIRY_MONTH, ""));
          cardPaymentMethod.setEncryptedExpiryYear(jsonObject.optString(ENCRYPTED_EXPIRY_YEAR, ""));
          cardPaymentMethod.setStoredPaymentMethodId(
              jsonObject.optString(STORED_PAYMENT_METHOD_ID));
          cardPaymentMethod.setEncryptedSecurityCode(
              jsonObject.optString(ENCRYPTED_SECURITY_CODE, ""));
          cardPaymentMethod.setHolderName(jsonObject.optString(HOLDER_NAME, ""));

          return cardPaymentMethod;
        }
      };

  private String encryptedCardNumber;
  private String encryptedExpiryMonth;
  private String encryptedExpiryYear;
  private String encryptedSecurityCode;
  private String holderName;
  private String storedPaymentMethodId;

  @Override public void writeToParcel(Parcel dest, int flags) {
    JsonUtils.writeToParcel(dest, SERIALIZER.serialize(this));
  }

  public String getEncryptedCardNumber() {
    return encryptedCardNumber;
  }

  public void setEncryptedCardNumber(String encryptedCardNumber) {
    this.encryptedCardNumber = encryptedCardNumber;
  }

  public String getEncryptedExpiryMonth() {
    return encryptedExpiryMonth;
  }

  public void setEncryptedExpiryMonth(String encryptedExpiryMonth) {
    this.encryptedExpiryMonth = encryptedExpiryMonth;
  }

  public String getEncryptedExpiryYear() {
    return encryptedExpiryYear;
  }

  public void setEncryptedExpiryYear(String encryptedExpiryYear) {
    this.encryptedExpiryYear = encryptedExpiryYear;
  }

  public String getEncryptedSecurityCode() {
    return encryptedSecurityCode;
  }

  public void setEncryptedSecurityCode(String encryptedSecurityCode) {
    this.encryptedSecurityCode = encryptedSecurityCode;
  }

  public String getHolderName() {
    return holderName;
  }

  public void setHolderName(String holderName) {
    this.holderName = holderName;
  }

  public String getStoredPaymentMethodId() {
    return this.storedPaymentMethodId;
  }

  public void setStoredPaymentMethodId(String storedPaymentMethodId) {
    this.storedPaymentMethodId = storedPaymentMethodId;
  }
}

