package com.appcoins.sdk.billing.helpers.translations;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class TranslationsXmlParser {

  private static final String translationsRelativePath =
      "appcoins-wallet/resources/translations/values-";
  private static final String translationsFileName = "/external_strings.xml";
  private Context context;

  public TranslationsXmlParser(Context context) {
    this.context = context;
  }

  List<String> parseTranslationXml(String language) {
    String translationXmlPath;
    translationXmlPath = translationsRelativePath + language + translationsFileName;

    InputStream inputStream;
    List<String> xmlContent = new ArrayList<>();
    try {
      inputStream = context.getAssets()
          .open(translationXmlPath);
      xmlContent = parseXml(inputStream);
      inputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return xmlContent;
  }

  public List<String> parseTranslationXmlWithPath(String path) {

    InputStream inputStream;
    List<String> xmlContent = new ArrayList<>();
    try {
      inputStream = context.getAssets()
          .open(path);
      xmlContent = parseXml(inputStream);
      inputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return xmlContent;
  }

  private List<String> parseXml(InputStream inputStream) throws IOException {
    List<String> xmlContent = new ArrayList<>();
    try {
      XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
      xmlPullParserFactory.setNamespaceAware(true);
      XmlPullParser parser = xmlPullParserFactory.newPullParser();
      parser.setInput(inputStream, null);
      int eventType = parser.getEventType();
      xmlContent = new ArrayList<>();
      while (eventType != XmlPullParser.END_DOCUMENT) {
        String value = parser.getText();
        if (eventType == XmlPullParser.TEXT && !value.trim()
            .isEmpty()) {
          xmlContent.add(value.trim());
        }
        eventType = parser.next();
      }
    } catch (Exception e) {
      inputStream.close();
      e.printStackTrace();
    }
    return xmlContent;
  }
}