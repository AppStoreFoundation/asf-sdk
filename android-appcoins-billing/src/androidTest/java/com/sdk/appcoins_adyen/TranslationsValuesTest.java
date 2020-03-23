package com.sdk.appcoins_adyen;

import android.content.Context;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import com.appcoins.sdk.billing.helpers.translations.TranslationsKeys;
import com.appcoins.sdk.billing.helpers.translations.TranslationsModel;
import com.appcoins.sdk.billing.helpers.translations.TranslationsXmlParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4ClassRunner.class) public class TranslationsValuesTest {

  @Test public void testDefaultStringMap() {
    Context context = InstrumentationRegistry.getInstrumentation()
        .getContext();
    TranslationsXmlParser translationsXmlParser = new TranslationsXmlParser(context);
    List<String> xmlDefaultStrings = removeSlashes(
        translationsXmlParser.parseTranslationXmlWithPath(
            "appcoins-wallet/resources/translations/values/external_strings.xml"));
    TranslationsModel translationsModel = new TranslationsModel();
    Map<TranslationsKeys, String> defaultStrings = translationsModel.getStringMap();
    assertEquals(defaultStrings.size(), xmlDefaultStrings.size());
    int position = 0;
    for (Map.Entry<TranslationsKeys, String> entry : defaultStrings.entrySet()) {
      Assert.assertEquals(entry.getValue(), xmlDefaultStrings.get(position));
      position++;
    }
  }

  @Test public void testTranslatedStringMap() {
    Context context = InstrumentationRegistry.getInstrumentation()
        .getContext();
    TranslationsXmlParser translationsXmlParser = new TranslationsXmlParser(context);
    List<String> xmlDefaultStrings = removeSlashes(
        translationsXmlParser.parseTranslationXmlWithPath(
            "appcoins-wallet/resources/translations/values-pt/external_strings.xml"));
    TranslationsModel translationsModel = new TranslationsModel();
    Map<TranslationsKeys, String> defaultStrings = translationsModel.getStringMap();
    assertEquals(defaultStrings.size(), xmlDefaultStrings.size());
    translationsModel.mapStrings(xmlDefaultStrings);
    int position = 0;
    for (Map.Entry<TranslationsKeys, String> entry : defaultStrings.entrySet()) {
      Assert.assertEquals(entry.getValue(), xmlDefaultStrings.get(position));
      position++;
    }
  }

  private List<String> removeSlashes(List<String> stringList) {
    List<String> modifiedList = new ArrayList<>();
    for (String string : stringList) {
      modifiedList.add(string.replace("\\", ""));
    }
    return modifiedList;
  }
}
