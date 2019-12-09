package com.appcoins.sdk.billing.helpers;

import java.io.Serializable;
import java.util.ArrayList;

public class TranslationsModel implements Serializable {

  private String languageCode;
  private String countryCode;
  private String installationButtonString;
  private String poaNotificationTitle;
  private String poaNotificationBody;
  private String iabInstallationDialogString;
  private String dialogStringHighlight;
  private String skipButtonString;

  public TranslationsModel(String languageCode, String countryCode) {
    this.languageCode = languageCode;
    this.countryCode = countryCode;
  }

  public void mapStrings(ArrayList<String> list) {
    iabInstallationDialogString = list.get(0);
    dialogStringHighlight = list.get(1);
    skipButtonString = list.get(2);
    installationButtonString = list.get(3);
    poaNotificationTitle = list.get(4);
    poaNotificationBody = list.get(5);
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

  public String getIabInstallationDialogString() {
    return iabInstallationDialogString;
  }

  public String getDialogStringHighlight() {
    return dialogStringHighlight;
  }

  public String getSkipButtonString() {
    return skipButtonString;
  }
}
