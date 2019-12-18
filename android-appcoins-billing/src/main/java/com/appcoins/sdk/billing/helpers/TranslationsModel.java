package com.appcoins.sdk.billing.helpers;

import java.io.Serializable;
import java.util.ArrayList;

public class TranslationsModel implements Serializable {

  private String languageCode;
  private String countryCode;
  private String installationButtonString;
  private String poaNotificationTitle;
  private String poaNotificationBody;
  private String installationDialogBody;
  private String dialogStringHighlight;
  private String skipButtonString;
  private String alertDialogMessage;
  private String alertDialogDismissButton;

  public TranslationsModel(String languageCode, String countryCode) {
    this.languageCode = languageCode;
    this.countryCode = countryCode;
  }

  public void mapStrings(ArrayList<String> list) {
    installationDialogBody = list.get(0);
    dialogStringHighlight = list.get(1);
    skipButtonString = list.get(2);
    installationButtonString = list.get(3);
    alertDialogMessage = list.get(4);
    alertDialogDismissButton = list.get(5);
    poaNotificationTitle = list.get(6);
    poaNotificationBody = list.get(7);
  }

  public String getLanguageCode() {
    return languageCode;
  }

  public String getCountryCode() {
    return countryCode;
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
}
