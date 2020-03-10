package com.sdk.appcoins_adyen;

import android.content.Context;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import com.appcoins.sdk.billing.helpers.translations.TranslationsModel;
import com.appcoins.sdk.billing.helpers.translations.TranslationsXmlParser;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4ClassRunner.class) public class TranslationsValuesTest {

  @Test public void testAppContext() {
    Context context = InstrumentationRegistry.getInstrumentation()
        .getContext();
    TranslationsXmlParser translationsXmlParser = new TranslationsXmlParser(context);
    List<String> xmlDefaultStrings = removeSlashes(
        translationsXmlParser.parseTranslationXmlWithPath(
            "appcoins-wallet/resources/translations/values/external_strings.xml"));
    TranslationsModel translationsModel = new TranslationsModel();
    List<String> defaultStrings = translationsModel.getStringsList();
    assertEquals(defaultStrings.size(), xmlDefaultStrings.size());
    for (int i = 0; i < defaultStrings.size(); i++) {
      assertEquals(defaultStrings.get(i), xmlDefaultStrings.get(i));
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
