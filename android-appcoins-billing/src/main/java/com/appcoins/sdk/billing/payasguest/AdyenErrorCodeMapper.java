package com.appcoins.sdk.billing.payasguest;

public class AdyenErrorCodeMapper {

  public AdyenErrorCodeMapper() {

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
        return "The transaction has been rejected by your bank. Please try with a different card "
            + "or contact us.";
      case 3: //Referral
      case 4: //Acquirer error
      case 9: //Issuer unavailable
        return "There was a problem with your card. Please try again or contact us.";
      case 6: //Expired Card
        return "It seems your card has expired. Please try with a different one.";
      case 7: //Invalid amount
      case 12: //Not enough balance
      case 25: //Restricted Card
        return "It seems you don\\'t have enough funds or there\\'s a limit on your card. Please "
            + "try with a different one.";
      case 8: //Invalid card number
        return "Are you sure your card number is correct? Please check and try again.";
      case 10: //Not supported
        return "Your card type is not supported yet. Try with a different one.";
      case 17: //Incorrect online pin
      case 18: //Pin tries exceeded
        return "Are you sure the security information is correct? Please try again.";
      default:
        return "Are you sure your card number is correct? Please check and try again.";
    }
  }
}
