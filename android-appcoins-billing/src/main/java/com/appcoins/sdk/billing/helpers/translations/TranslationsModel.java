package com.appcoins.sdk.billing.helpers.translations;

import java.util.List;

public class TranslationsModel {

  private static final int NUMBER_OF_TRANSLATED_STRINGS = 32;
  // SDK - Purchasing when wallet is not installed
  private String installationDialogBody = "To buy this item you first need to get the %s.";
  private String dialogStringHighlight = "AppCoins Wallet";
  private String skipButtonString = "CLOSE";
  //SDK - Purchasing when neither wallet or app store are installed
  private String alertDialogMessage =
      "You need the AppCoins Wallet to make this purchase. Download it from Aptoide or Play Store"
          + " and come back to complete your purchase!";
  private String alertDialogDismissButton = "GOT IT!";
  //SDK - PoA when wallet is not installed
  private String poaNotificationTitle = "You need the AppCoins Wallet!";
  private String poaNotificationBody = "To get your reward you need the AppCoins Wallet.";
  //SDK - Pay as a guest
  private String payAsGuestTitle = "Pay as a guest";
  private String creditCard = "Credit Card";
  private String paypal = "PayPal";
  private String switchToPaypal = "PAY USING PAYPAL";
  private String switchToCreditCard = "PAY USING CREDIT CARD";
  private String cardNumber = "Card number";
  private String expiryDate = "MM/YY";
  private String cardCvv = "CVV";
  private String cardCvvCvc = "CVC/CVV";
  private String changeCard = "CHANGE CARD";
  private String payWithWalletTitle = "Pay with AppCoins Wallet";
  private String walletRewardTitle = "Get up to %s%% Bonus!";
  private String walletRewardBody = "You\'ll receive %s on this purchase.";
  private String noConnectionWalletRewardBody = "You\'ll receive a Bonus on this purchase.";
  private String bestDeal = "BEST DEAL";
  private String doneTitle = "DONE!";
  private String purchaseDoneRewardTitle =
      "Next time, get up to %s%% Bonus with the AppCoins Wallet!";
  private String noConnectionDoneRewardTitle = "Next time, get a Bonus with the AppCoins Wallet!";
  private String doneRewardBody = "You could have received %s on this purchase.";
  private String doneTitleLong = "Purchase completed!";
  private String morePaymentMethods = "MORE PAYMENT METHODS";
  //SDK - Common
  private String nextButton = "NEXT";
  private String cancelButton = "CANCEL";
  private String buyButton = "BUY";
  private String installationButtonString = "INSTALL WALLET";

  TranslationsModel() {
  }

  void mapStrings(List<String> list) {
    int offset = 0;
    if (list.size() == NUMBER_OF_TRANSLATED_STRINGS - 2) {//Some files don't have all strings
      offset = 2;
    }
    if (list.size() >= NUMBER_OF_TRANSLATED_STRINGS - 2) {
      installationButtonString = list.get(0);
      dialogStringHighlight = list.get(1);
      skipButtonString = list.get(2);
      if (offset != 2) {
        alertDialogMessage = list.get(3);
        alertDialogDismissButton = list.get(4);
      }
      poaNotificationTitle = list.get(5 - offset);
      poaNotificationBody = list.get(6 - offset);
      payAsGuestTitle = list.get(7 - offset);
      creditCard = list.get(8 - offset);
      paypal = list.get(9 - offset);
      switchToPaypal = list.get(10 - offset);
      switchToCreditCard = list.get(11 - offset);
      cardNumber = list.get(12 - offset);
      expiryDate = list.get(13 - offset);
      cardCvv = list.get(14 - offset);
      cardCvvCvc = list.get(15 - offset);
      changeCard = list.get(16 - offset);
      payWithWalletTitle = list.get(17 - offset);
      walletRewardTitle = list.get(18 - offset);
      walletRewardBody = list.get(19 - offset);
      noConnectionWalletRewardBody = list.get(20 - offset);
      bestDeal = list.get(21 - offset);
      doneTitle = list.get(22 - offset);
      purchaseDoneRewardTitle = list.get(23 - offset);
      noConnectionDoneRewardTitle = list.get(24 - offset);
      doneRewardBody = list.get(25 - offset);
      doneTitleLong = list.get(26 - offset);
      morePaymentMethods = list.get(27 - offset);
      nextButton = list.get(28 - offset);
      cancelButton = list.get(29 - offset);
      buyButton = list.get(30 - offset);
      installationDialogBody = list.get(31 - offset);
    }
  }

  public String getInstallationButtonString() {
    return installationButtonString;
  }

  public String getPoaNotificationTitle() {
    return poaNotificationTitle;
  }

  public String getPoaNotificationBody() {
    return poaNotificationBody;
  }

  public String getInstallationDialogBody() {
    return installationDialogBody;
  }

  public String getDialogStringHighlight() {
    return dialogStringHighlight;
  }

  public String getSkipButtonString() {
    return skipButtonString;
  }

  public String getAlertDialogMessage() {
    return alertDialogMessage;
  }

  public String getAlertDialogDismissButton() {
    return alertDialogDismissButton;
  }

  public String getPayAsGuestTitle() {
    return payAsGuestTitle;
  }

  public String getCreditCard() {
    return creditCard;
  }

  public String getPaypal() {
    return paypal;
  }

  public String getSwitchToPaypal() {
    return switchToPaypal;
  }

  public String getSwitchToCreditCard() {
    return switchToCreditCard;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public String getExpiryDate() {
    return expiryDate;
  }

  public String getCardCvv() {
    return cardCvv;
  }

  public String getCardCvvCvc() {
    return cardCvvCvc;
  }

  public String getChangeCard() {
    return changeCard;
  }

  public String getPayWithWalletTitle() {
    return payWithWalletTitle;
  }

  public String getWalletRewardTitle() {
    return walletRewardTitle;
  }

  public String getWalletRewardBody() {
    return walletRewardBody;
  }

  public String getNoConnectionWalletRewardBody() {
    return noConnectionWalletRewardBody;
  }

  public String getBestDeal() {
    return bestDeal;
  }

  public String getDoneTitle() {
    return doneTitle;
  }

  public String getPurchaseDoneRewardTitle() {
    return purchaseDoneRewardTitle;
  }

  public String getNoConnectionDoneRewardTitle() {
    return noConnectionDoneRewardTitle;
  }

  public String getDoneRewardBody() {
    return doneRewardBody;
  }

  public String getDoneTitleLong() {
    return doneTitleLong;
  }

  public String getMorePaymentMethods() {
    return morePaymentMethods;
  }

  public String getNextButton() {
    return nextButton;
  }

  public String getCancelButton() {
    return cancelButton;
  }

  public String getBuyButton() {
    return buyButton;
  }
}
