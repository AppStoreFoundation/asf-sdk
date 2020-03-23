package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.helpers.translations.TranslationsKeys;
import com.appcoins.sdk.billing.helpers.translations.TranslationsModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.error_contac_us_body;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_wallet_not_installed_popup_body;

public class TranslationsTest {

  private TranslationsModel translationsModel;
  private Map<TranslationsKeys, String> defaultValues;
  private List<String> wrongBelowSizeList;
  private List<String> correctSizeList;
  private ArrayList<String> wrongAboveSizeList;

  @Before public void setupTest() {
    translationsModel = new TranslationsModel();
    defaultValues = translationsModel.getStringMap();
    correctSizeList = new ArrayList<>();
    wrongAboveSizeList = new ArrayList<>();
    wrongBelowSizeList = Arrays.asList("value", "value2");
    for (int i = 0; i < translationsModel.getNumberOfTranslations(); i++) {
      String value = "value" + i;
      correctSizeList.add(value);
    }
    for (int i = 0; i < translationsModel.getNumberOfTranslations() + 2; i++) {
      String value = "value" + i;
      wrongAboveSizeList.add(value);
    }
  }

  @Test public void wrongBelowSizeListTest() {
    translationsModel.mapStrings(wrongBelowSizeList);
    Assert.assertEquals("If the problem persists please contact us.",
        translationsModel.getString(error_contac_us_body));
    Assert.assertEquals("value", translationsModel.getString(iab_wallet_not_installed_popup_body));
  }

  @Test public void wrongAboveSizeListTest() {
    translationsModel.mapStrings(wrongAboveSizeList);
    int position = 0;
    for (Map.Entry<TranslationsKeys, String> entry : defaultValues.entrySet()) {
      Assert.assertEquals(entry.getValue(), wrongAboveSizeList.get(position));
      position++;
    }
  }

  @Test public void correctSizeListTest() {
    translationsModel.mapStrings(correctSizeList);
    int position = 0;
    for (Map.Entry<TranslationsKeys, String> entry : defaultValues.entrySet()) {
      Assert.assertEquals(entry.getValue(), correctSizeList.get(position));
      position++;
    }
  }
}
