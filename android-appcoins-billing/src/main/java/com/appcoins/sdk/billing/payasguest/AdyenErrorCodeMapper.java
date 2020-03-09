package com.appcoins.sdk.billing.payasguest;

import com.appcoins.sdk.billing.helpers.translations.TranslationsModel;

public class AdyenErrorCodeMapper {

  private TranslationsModel translationsModel;

  AdyenErrorCodeMapper(TranslationsModel translationsModel) {
    this.translationsModel = translationsModel;
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
        return translationsModel.getAdyenGeneralError2();
      case 3: //Referral
      case 4: //Acquirer error
      case 9: //Issuer unavailable
        return translationsModel.getAdyenGeneralError1();
      case 6: //Expired Card
        return translationsModel.getExpiredError();
      case 7: //Invalid amount
      case 12: //Not enough balance
      case 25: //Restricted Card
        return translationsModel.getNoFundsError();
      case 10: //Not supported
        return translationsModel.getCardNotSupportedError();
      case 17: //Incorrect online pin
      case 18: //Pin tries exceeded
        return translationsModel.getSecurityError();
      case 8: //Invalid card number
      default:
        return translationsModel.getInvalidDetailsError();
    }
  }
}
