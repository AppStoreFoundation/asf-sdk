package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.helpers.translations.TranslationsModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TranslationsTest {

  private TranslationsModel translationsModel;
  private List<String> defaultValuesList;
  private List<String> wrongSizeList;
  private List<String> incompleteSizeList;
  private List<String> correctSizeList;

  @Before public void setupTest() {
    translationsModel = new TranslationsModel();
    defaultValuesList = new ArrayList<>(translationsModel.getStringsList());
    incompleteSizeList = new ArrayList<>();
    correctSizeList = new ArrayList<>();
    wrongSizeList = Arrays.asList("value", "value2");
    for (int i = 0; i < TranslationsModel.NUMBER_OF_TRANSLATED_STRINGS; i++) {
      String value = "value" + i;
      incompleteSizeList.add(value);
      correctSizeList.add(value);
    }
    incompleteSizeList.remove(0);
    incompleteSizeList.remove(1);
  }

  @Test public void wrongSizeListTest() {
    translationsModel.mapStrings(wrongSizeList);
    Assert.assertEquals("If the problem persists please contact us.",
        translationsModel.getContactUsError());
    Assert.assertNotEquals("value", translationsModel.getInstallationDialogBody());
  }

  //Remove this test if the problem with not having the same strings in every file is fixed
  @Test public void incompleteSizeListTest() {
    translationsModel.mapStrings(incompleteSizeList);
    Assert.assertEquals("value" + (TranslationsModel.NUMBER_OF_TRANSLATED_STRINGS - 1),
        translationsModel.getContactUsError());
    Assert.assertEquals("value6", translationsModel.getPoaNotificationBody());
  }

  @Test public void correctSizeListTest() {
    translationsModel.mapStrings(correctSizeList);
    for (int i = 0; i < TranslationsModel.NUMBER_OF_TRANSLATED_STRINGS; i++) {
      Assert.assertNotEquals(defaultValuesList.get(i), correctSizeList.get(i));
    }
  }

  @Test public void correctDefaultListSizeTest() {
    Assert.assertEquals(defaultValuesList.size(), TranslationsModel.NUMBER_OF_TRANSLATED_STRINGS);
  }
}
