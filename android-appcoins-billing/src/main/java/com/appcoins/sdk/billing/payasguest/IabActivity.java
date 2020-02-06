package com.appcoins.sdk.billing.payasguest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper;
import com.appcoins.sdk.billing.helpers.TranslationsModel;
import com.appcoins.sdk.billing.helpers.TranslationsXmlParser;
import java.util.Locale;

public class IabActivity extends Activity {

  private final static String TRANSLATIONS = "translations";
  private AppcoinsBillingStubHelper appcoinsBillingStubHelper;
  private BuyItemProperties buyItemProperties;
  private TranslationsModel translationsModel;

  @SuppressLint("ResourceType") @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    appcoinsBillingStubHelper = AppcoinsBillingStubHelper.getInstance();
    buyItemProperties = (BuyItemProperties) getIntent().getSerializableExtra(
        AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES);

    //This log is necessary for the automatic test that validates the wallet installation dialog
    Log.d("InstallDialog", "com.appcoins.sdk.billing.helpers.InstallDialogActivity started");

    if (savedInstanceState != null) {
      translationsModel = (TranslationsModel) savedInstanceState.get(TRANSLATIONS);
    } else {
      fetchTranslations();
    }

    int backgroundColor = Color.parseColor("#64000000");
    FrameLayout frameLayout = new FrameLayout(this);
    frameLayout.setId(3);
    frameLayout.setBackgroundColor(backgroundColor);

    setContentView(frameLayout);

    navigateTo(new PaymentMethodsFragment(), frameLayout);
  }

  private void fetchTranslations() {
    Locale locale = Locale.getDefault();
    if (translationsModel == null || !translationsModel.getLanguageCode()
        .equalsIgnoreCase(locale.getLanguage()) || !translationsModel.getCountryCode()
        .equalsIgnoreCase(locale.getCountry())) {
      TranslationsXmlParser translationsParser = new TranslationsXmlParser(this);
      translationsModel =
          translationsParser.parseTranslationXml(locale.getLanguage(), locale.getCountry());
    }
  }

  @SuppressLint("ResourceType")
  private void navigateTo(Fragment fragment, FrameLayout frameLayout) {
    getFragmentManager().beginTransaction()
        .replace(frameLayout.getId(), fragment)
        .commit();
  }
}
