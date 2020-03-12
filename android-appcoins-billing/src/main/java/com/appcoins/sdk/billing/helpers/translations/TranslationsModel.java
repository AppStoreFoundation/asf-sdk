package com.appcoins.sdk.billing.helpers.translations;

import java.util.Arrays;
import java.util.List;

public class TranslationsModel {

  public static final int NUMBER_OF_TRANSLATED_STRINGS = 47;
  private List<String> stringsList;

  public TranslationsModel() {
    stringsList = Arrays.asList("To buy this item you first need to get the %s.",//0
        "AppCoins Wallet", //1
        "CLOSE", //2
        "You need the AppCoins Wallet to make this purchase. Download it from Aptoide or Play "
            + "Store"
            + " and come back to complete your purchase!", //3
        "GOT IT!", //4
        "You need the AppCoins Wallet!", //5
        "To get your reward you need the AppCoins Wallet.", //6
        "Pay as a guest", //7
        "Credit Card", //8
        "PayPal", //9
        "PAY USING PAYPAL", //10
        "PAY USING CREDIT CARD", //11
        "Card number", //12
        "MM/YY", //13
        "CVV", //14
        "CVC/CVV", //15
        "CHANGE CARD", //16
        "Pay with AppCoins Wallet", //17
        "Get up to %s%% Bonus!", //18
        "You\'ll receive %s on this purchase.", //19
        "You'll receive a Bonus on this purchase.", //20
        "BEST DEAL", //21
        "DONE!", //22
        "Next time, get up to %s%% Bonus with the AppCoins Wallet!", //23
        "Next time, get a Bonus with the AppCoins Wallet!", //24
        "You could have received %s on this purchase.", //25
        "Purchase completed!", //26
        "MORE PAYMENT METHODS", //27
        "NEXT", //28
        "CANCEL", //29
        "BUY", //30
        "INSTALL WALLET", //31
        "OK", //32
        "Install", //33
        "Error", //34
        "You already own this item!", //35
        "Oops, something went wrong.", //36
        "There was a problem with your card. Please try again or contact us.", //37
        "The transaction has been rejected by your bank. Please try with a different card or "
            + "contact us.", //38
        "It seems you don't have enough funds or there's a limit on your card. Please try "
            + "with a "
            + "different one.", //39
        "It seems your card has expired. Please try with a different one.", //40
        "Are you sure your card number is correct? Please check and try again.", //41
        "Your card type is not supported yet. Try with a different one.", //42
        "Are you sure the security information is correct? Please try again.", //43
        "Your CVV/CVC code seems to be wrong. Please try again.", //44
        "Wrong CVV/CVC", //45
        "If the problem persists please contact us." //46
    );
  }

  public void mapStrings(List<String> list) {
    for (int i = 0; i < list.size(); i++) {
      stringsList.set(i, list.get(i));
    }
  }

  public String getInstallationDialogBody() {
    return stringsList.get(0);
  }

  public String getDialogStringHighlight() {
    return stringsList.get(1);
  }

  public String getSkipButtonString() {
    return stringsList.get(2);
  }

  public String getAlertDialogMessage() {
    return stringsList.get(3);
  }

  public String getAlertDialogDismissButton() {
    return stringsList.get(4);
  }

  public String getPoaNotificationTitle() {
    return stringsList.get(5);
  }

  public String getPoaNotificationBody() {
    return stringsList.get(6);
  }

  public String getPayAsGuestTitle() {
    return stringsList.get(7);
  }

  public String getCreditCard() {
    return stringsList.get(8);
  }

  public String getPaypal() {
    return stringsList.get(9);
  }

  public String getSwitchToPaypal() {
    return stringsList.get(10);
  }

  public String getSwitchToCreditCard() {
    return stringsList.get(11);
  }

  public String getCardNumber() {
    return stringsList.get(12);
  }

  public String getExpiryDate() {
    return stringsList.get(13);
  }

  public String getCardCvv() {
    return stringsList.get(14);
  }

  public String getCardCvvCvc() {
    return stringsList.get(15);
  }

  public String getChangeCard() {
    return stringsList.get(16);
  }

  public String getPayWithWalletTitle() {
    return stringsList.get(17);
  }

  public String getWalletRewardTitle() {
    return stringsList.get(18);
  }

  public String getWalletRewardBody() {
    return stringsList.get(19);
  }

  public String getNoConnectionWalletRewardBody() {
    return stringsList.get(20);
  }

  public String getBestDeal() {
    return stringsList.get(21);
  }

  public String getDoneTitle() {
    return stringsList.get(22);
  }

  public String getPurchaseDoneRewardTitle() {
    return stringsList.get(23);
  }

  public String getNoConnectionDoneRewardTitle() {
    return stringsList.get(24);
  }

  public String getDoneRewardBody() {
    return stringsList.get(25);
  }

  public String getDoneTitleLong() {
    return stringsList.get(26);
  }

  public String getMorePaymentMethods() {
    return stringsList.get(27);
  }

  public String getNextButton() {
    return stringsList.get(28);
  }

  public String getCancelButton() {
    return stringsList.get(29);
  }

  public String getBuyButton() {
    return stringsList.get(30);
  }

  public String getInstallationButtonString() {
    return stringsList.get(31);
  }

  public String getOkButton() {
    return stringsList.get(32);
  }

  public String getInstallButton() {
    return stringsList.get(33)
        .toUpperCase();
  }

  public String getErrorTitle() {
    return stringsList.get(34);
  }

  public String getItemAlreadyOwned() {
    return stringsList.get(35);
  }

  public String getGenericError() {
    return stringsList.get(36);
  }

  public String getAdyenGeneralError1() {
    return stringsList.get(37);
  }

  public String getAdyenGeneralError2() {
    return stringsList.get(38);
  }

  public String getNoFundsError() {
    return stringsList.get(39);
  }

  public String getExpiredError() {
    return stringsList.get(40);
  }

  public String getInvalidDetailsError() {
    return stringsList.get(41);
  }

  public String getCardNotSupportedError() {
    return stringsList.get(42);
  }

  public String getSecurityError() {
    return stringsList.get(43);
  }

  public String getWrongCVVError() {
    return stringsList.get(44);
  }

  public String getCvvError() {
    return stringsList.get(45);
  }

  public String getContactUsError() {
    return stringsList.get(46);
  }

  public List<String> getStringsList() {
    return stringsList;
  }
}
