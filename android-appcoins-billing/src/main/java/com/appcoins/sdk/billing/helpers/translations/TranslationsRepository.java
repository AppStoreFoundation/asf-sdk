package com.appcoins.sdk.billing.helpers.translations;

import android.content.Context;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.helpers.WalletUtils;
import java.util.List;

public class TranslationsRepository {

  private static TranslationsRepository translationsRepositoryInstance = null;
  private final TranslationsModel translationsModel;
  private final TranslationsXmlParser translationsXmlParser;
  private String languageCode = "";
  private String countryCode = "";

  private TranslationsRepository(TranslationsModel translationsModel,
      TranslationsXmlParser translationsXmlParser) {
    this.translationsModel = translationsModel;
    this.translationsXmlParser = translationsXmlParser;
  }

  public static TranslationsRepository getInstance(Context context, String languageCode) {
    if (translationsRepositoryInstance == null) {
      translationsRepositoryInstance =
          new TranslationsRepository(new TranslationsModel(), new TranslationsXmlParser(context));
      translationsRepositoryInstance.fetchTranslations(languageCode);
    }
    return translationsRepositoryInstance;
  }

  public TranslationsModel getTranslationsModel() {
    return translationsModel;
  }

  private void fetchTranslations(String languageCode) {
    if (WalletUtils.getIabAction()
        .equals(BuildConfig.CAFE_BAZAAR_IAB_BIND_ACTION)) {
      if (needsToRefreshModel(languageCode) || languageFromIran()) {
        translate("fa");
      }
    }
    if (needsToRefreshModel(languageCode)) {
      translate(languageCode);
    }
  }

  private void translate(String languageCode) {
    this.languageCode = languageCode;
    List<String> translationList = translationsXmlParser.parseTranslationXml(languageCode);
    translationsModel.mapStrings(translationList);
  }

  private boolean needsToRefreshModel(String languageCode) {
    return !this.languageCode.equalsIgnoreCase(languageCode);
  }

  private boolean languageFromIran() {
    return !this.languageCode.equalsIgnoreCase("fa");
  }
}
