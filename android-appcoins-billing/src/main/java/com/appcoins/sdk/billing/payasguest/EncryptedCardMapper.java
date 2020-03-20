package com.appcoins.sdk.billing.payasguest;

import com.sdk.appcoins_adyen.card.EncryptedCard;

public class EncryptedCardMapper {

  EncryptedCardMapper() {

  }

  public String map(EncryptedCard encryptedCard) {
    return " {\n\t\t\"encryptedCardNumber\":"
        + "\""
        + encryptedCard.getEncryptedNumber()
        + "\""
        + ","
        + "\n\t\t\"encryptedExpiryMonth"
        + "\":"
        + "\""
        + encryptedCard.getEncryptedExpiryMonth()
        + "\""
        + ","
        + "\n\t\t\"encryptedExpiryYear\":"
        + "\""
        + encryptedCard.getEncryptedExpiryYear()
        + "\""
        + ",\n\t\t\"encryptedSecurityCode\":"
        + "\""
        + encryptedCard.getEncryptedSecurityCode()
        + "\""
        + ",\n\t\t\"type\":\"scheme\"\n\t}";
  }
}
