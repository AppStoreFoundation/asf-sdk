package com.appcoins.sdk.billing.payasguest;

import com.appcoins.sdk.billing.helpers.translations.TranslationsRepository;

import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_expired;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_general_1;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_general_2;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_invalid_details;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_no_funds;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_not_supported;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_security;

public class AdyenErrorCodeMapper {

  private TranslationsRepository translations;

  AdyenErrorCodeMapper(TranslationsRepository translations) {
    this.translations = translations;
  }

  public String map(int errorCode) {
    switch (errorCode) {
      case 2: //Declined
      case 5: //Blocked Card
      case 20: //Fraud
      case 22: //Cancelled due to fraud
      case 23: //Transition not permited
      case 26: //Revocation of Auth
      case 27: //Declined non generic
      case 31: //Issuer suspected fraud
        return translations.getString(purchase_card_error_general_2);
      case 3: //Referral
      case 4: //Acquirer error
      case 9: //Issuer unavailable
        return translations.getString(purchase_card_error_general_1);
      case 6: //Expired Card
        return translations.getString(purchase_card_error_expired);
      case 7: //Invalid amount
      case 12: //Not enough balance
      case 25: //Restricted Card
        return translations.getString(purchase_card_error_no_funds);
      case 10: //Not supported
        return translations.getString(purchase_card_error_not_supported);
      case 17: //Incorrect online pin
      case 18: //Pin tries exceeded
        return translations.getString(purchase_card_error_security);
      case 8: //Invalid card number
      default:
        return translations.getString(purchase_card_error_invalid_details);
    }
  }
}
