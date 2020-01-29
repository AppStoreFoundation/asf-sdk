package com.appcoins.sdk.billing.helpers;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class TranslationsXmlParser {

  private static final String defaultInstallationButtonText = "INSTALL WALLET";
  private static final String defaultSkipButtonText = "CLOSE";
  private static final String defaultNotificationTitle = "You need the AppCoins Wallet!";
  private static final String defaultNotificationBody =
      "To get your reward you need the AppCoins Wallet.";
  private static final String defaultDialogBody = "To buy this item you first need to get the %s.";
  private static final String defaultHighlightText = "AppCoins Wallet";
  private static final String defaultAlertDialogMessage =
      "You need the AppCoins Wallet to make this purchase. Download it from Aptoide or Play Store"
          + " and come back to complete your purchase!";
  private static final String defaultAlertDialogDismissButton = "GOT IT!";

  private static final String translationsRelativePath =
      "appcoins-wallet/resources/translations/values-";
  private static final String translationsFileName = "/external_strings.xml";
  private static final int NUMBER_OF_TRANSLATED_STRINGS = 8;
  private Context context;

  public TranslationsXmlParser(Context context) {
    this.context = context;
  }

  public TranslationsModel parseTranslationXml(String language, String country) {
    String translationXmlPath;
    TranslationsModel translationsModel = new TranslationsModel(language, country);
    if (language.equalsIgnoreCase(country)) {
      translationXmlPath = translationsRelativePath + language + translationsFileName;
    } else {
      translationXmlPath =
          translationsRelativePath + language + "-r" + country.toUpperCase() + translationsFileName;
    }
    try {
      InputStream inputStream = context.getAssets()
          .open(translationXmlPath);
      ArrayList<String> xmlContent = parseXml(inputStream);
      if (xmlContent.size() == NUMBER_OF_TRANSLATED_STRINGS) {
        translationsModel.mapStrings(xmlContent);
      } else {
        fillInMissingStrings(xmlContent);
        translationsModel.mapStrings(xmlContent);
      }
      inputStream.close();
    } catch (XmlPullParserException e) {
      translationsModel.mapStrings(setDefaultValues());
      e.printStackTrace();
    } catch (IOException e) {
      translationsModel.mapStrings(setDefaultValues());
      e.printStackTrace();
    }
    return translationsModel;
  }

  private ArrayList<String> parseXml(InputStream inputStream)
      throws XmlPullParserException, IOException {
    XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
    xmlPullParserFactory.setNamespaceAware(true);
    XmlPullParser parser = xmlPullParserFactory.newPullParser();
    parser.setInput(inputStream, null);
    int eventType = parser.getEventType();
    ArrayList<String> xmlContent = new ArrayList<>();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      String value = parser.getText();
      if (eventType == XmlPullParser.TEXT && !value.trim()
          .isEmpty()) {
        xmlContent.add(value.trim());
      }
      eventType = parser.next();
    }
    return xmlContent;
  }

  private ArrayList<String> setDefaultValues() {
    ArrayList<String> defaultValues = new ArrayList<>();
    defaultValues.add(defaultDialogBody);
    defaultValues.add(defaultHighlightText);
    defaultValues.add(defaultSkipButtonText);
    defaultValues.add(defaultInstallationButtonText);
    defaultValues.add(defaultAlertDialogMessage);
    defaultValues.add(defaultAlertDialogDismissButton);
    defaultValues.add(defaultNotificationTitle);
    defaultValues.add(defaultNotificationBody);
    return defaultValues;
  }

  private void fillInMissingStrings(ArrayList<String> xmlContent) {
    xmlContent.add(4, defaultAlertDialogMessage);
    xmlContent.add(5, defaultAlertDialogDismissButton);
  }
}