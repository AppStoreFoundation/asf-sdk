package com.sdk.appcoins_adyen.encryption;

import com.sdk.appcoins_adyen.card.Card;
import com.sdk.appcoins_adyen.exceptions.EncrypterException;
import com.sdk.appcoins_adyen.exceptions.EncryptionException;
import java.util.Date;

public final class CardEncryptorImpl {

  private String publicKey;

  public CardEncryptorImpl(String publicKey) {
    this.publicKey = publicKey;
  }

  public String encryptFields(String number, Integer month, Integer year, String code)
      throws EncryptionException {
    String encryptedExpiryMonth = "";
    String encryptedExpiryYear = "";
    String encryptedNumber = "";
    String encryptedSecurityCode = "";
    try {
      Date generationTime = new Date();
      if (number != null) {
        try {
          encryptedNumber = (new Card.Builder()).setNumber(number)
              .setGenerationTime(generationTime)
              .build()
              .serialize(publicKey);
        } catch (RuntimeException var12) {
          throw new EncryptionException("Encryption failed.", var12);
        }
      }

      if (month != null && year != null) {
        encryptedExpiryMonth = (new Card.Builder()).setExpiryMonth(String.valueOf(month))
            .setGenerationTime(generationTime)
            .build()
            .serialize(publicKey);
        encryptedExpiryYear = (new Card.Builder()).setExpiryYear(String.valueOf(year))
            .setGenerationTime(generationTime)
            .build()
            .serialize(publicKey);
      } else {
        if (month != null || year != null) {
          throw new EncryptionException(
              "Both expiryMonth and expiryYear need to be set for encryption.", null);
        }

        encryptedExpiryMonth = null;
        encryptedExpiryYear = null;
      }

      encryptedSecurityCode = (new Card.Builder()).setCvc(code)
          .setGenerationTime(generationTime)
          .build()
          .serialize(publicKey);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String json = " {\n\t\t\"encryptedCardNumber\":"
        + "\""
        + encryptedNumber
        + "\""
        + ","
        + "\n\t\t\"encryptedExpiryMonth"
        + "\":"
        + "\""
        + encryptedExpiryMonth
        + "\""
        + ","
        + "\n\t\t\"encryptedExpiryYear\":"
        + "\""
        + encryptedExpiryYear
        + "\""
        + ",\n\t\t\"encryptedSecurityCode\":"
        + "\""
        + encryptedSecurityCode
        + "\""
        + ",\n\t\t\"type\":\"scheme\"\n\t}";
    return json;
  }

  public String encryptStoredPaymentFields(String securityCode, String paymentId, String type) {
    Date generationTime = new Date();
    String encryptedSecurityCode = "";
    try {
      encryptedSecurityCode = (new Card.Builder()).setCvc(securityCode)
          .setGenerationTime(generationTime)
          .build()
          .serialize(publicKey);
    } catch (EncrypterException e) {
      e.printStackTrace();
    }
    String json = " {\n\t\t\"encryptedSecurityCode\":"
        + "\""
        + encryptedSecurityCode
        + "\""
        + ","
        + "\n\t\t\"storedPaymentMethodId"
        + "\":"
        + "\""
        + paymentId
        + "\""
        + ","
        + "\n\t\t\"type\":"
        + "\""
        + type
        + "\""
        + "\n\t}";
    return json;
  }
}
