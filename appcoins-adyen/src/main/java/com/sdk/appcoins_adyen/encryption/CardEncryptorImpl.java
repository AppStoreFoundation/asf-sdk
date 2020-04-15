package com.sdk.appcoins_adyen.encryption;

import com.sdk.appcoins_adyen.card.Card;
import com.sdk.appcoins_adyen.card.EncryptedCard;
import com.sdk.appcoins_adyen.exceptions.EncrypterException;
import com.sdk.appcoins_adyen.exceptions.EncryptionException;
import java.util.Date;

public final class CardEncryptorImpl {

  private String publicKey;

  public CardEncryptorImpl(String publicKey) {
    this.publicKey = publicKey;
  }

  public EncryptedCard encryptFields(String number, Integer month, Integer year, String code)
      throws EncryptionException {
    try {
      Date generationTime = new Date();
      String cardNumber = number;
      String encryptedNumber = null;
      if (cardNumber != null) {
        try {
          encryptedNumber = (new Card.Builder()).setNumber(cardNumber)
              .setGenerationTime(generationTime)
              .build()
              .serialize(publicKey);
        } catch (RuntimeException var12) {
          throw new EncryptionException("Encryption failed.", var12);
        }
      }

      Integer expiryMonth = month;
      Integer expiryYear = year;
      String encryptedExpiryMonth;
      String encryptedExpiryYear;
      if (expiryMonth != null && expiryYear != null) {
        encryptedExpiryMonth = (new Card.Builder()).setExpiryMonth(String.valueOf(expiryMonth))
            .setGenerationTime(generationTime)
            .build()
            .serialize(publicKey);
        encryptedExpiryYear = (new Card.Builder()).setExpiryYear(String.valueOf(expiryYear))
            .setGenerationTime(generationTime)
            .build()
            .serialize(publicKey);
      } else {
        if (expiryMonth != null || expiryYear != null) {
          throw new EncryptionException(
              "Both expiryMonth and expiryYear need to be set for encryption.", null);
        }

        encryptedExpiryMonth = null;
        encryptedExpiryYear = null;
      }

      String encryptedSecurityCode = (new Card.Builder()).setCvc(code)
          .setGenerationTime(generationTime)
          .build()
          .serialize(publicKey);
      EncryptedCard.Builder builder =
          (new EncryptedCard.Builder()).setEncryptedNumber(encryptedNumber);
      if (encryptedExpiryMonth != null && encryptedExpiryYear != null) {
        builder.setEncryptedExpiryDate(encryptedExpiryMonth, encryptedExpiryYear);
      } else {
        builder.clearEncryptedExpiryDate();
      }

      return builder.setEncryptedSecurityCode(encryptedSecurityCode)
          .build();
    } catch (IllegalStateException | EncrypterException var13) {
      throw new EncryptionException(var13.getMessage(), var13.getCause());
    }
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
