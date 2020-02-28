package com.appcoins.sdk.billing.payasguest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper;
import com.appcoins.sdk.billing.helpers.TranslationsModel;
import com.appcoins.sdk.billing.helpers.TranslationsXmlParser;
import com.appcoins.sdk.billing.helpers.Utils;
import java.util.Locale;

import static com.appcoins.sdk.billing.helpers.Utils.RESPONSE_CODE;

public class IabActivity extends Activity implements IabView {

  private final static String TRANSLATIONS = "translations";
  private BuyItemProperties buyItemProperties;
  private TranslationsModel translationsModel;

  @SuppressLint("ResourceType") @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //This log is necessary for the automatic test that validates the wallet installation dialog
    Log.d("InstallDialog", "com.appcoins.sdk.billing.helpers.InstallDialogActivity started");

    int backgroundColor = Color.parseColor("#64000000");
    FrameLayout frameLayout = new FrameLayout(this);
    frameLayout.setId(3);
    frameLayout.setBackgroundColor(backgroundColor);

    setContentView(frameLayout);

    buyItemProperties = (BuyItemProperties) getIntent().getSerializableExtra(
        AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES);

    if (savedInstanceState != null) {
      translationsModel = (TranslationsModel) savedInstanceState.get(TRANSLATIONS);
    } else {
      fetchTranslations();
      navigateTo(PaymentMethodsFragment.newInstance(buyItemProperties), frameLayout);
    }
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

  private void navigateTo(Fragment fragment, FrameLayout frameLayout) {
    getFragmentManager().beginTransaction()
        .replace(frameLayout.getId(), fragment)
        .commit();
  }

  @Override public TranslationsModel getTranslationsModel() {
    return translationsModel;
  }

  @Override public void close() {
    Bundle bundle = new Bundle();
    bundle.putInt(RESPONSE_CODE, 1); //CANCEL
    Intent intent = new Intent();
    intent.putExtras(bundle);
    setResult(Activity.RESULT_CANCELED, intent);
    finish();
  }

  @Override public void showAlertNoBrowserAndStores() {
    buildAlertNoBrowserAndStores();
  }

  @Override public void redirectToWalletInstallation(Intent intent) {
    startActivity(intent);
  }

  @Override public void navigateToAdyen(String selectedRadioButton) {

  }

  private void buildAlertNoBrowserAndStores() {
    AlertDialog.Builder alert = new AlertDialog.Builder(this);
    String value = translationsModel.getAlertDialogMessage();
    String dismissValue = translationsModel.getAlertDialogDismissButton();
    alert.setMessage(value);
    alert.setCancelable(true);
    alert.setPositiveButton(dismissValue, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        Bundle response = new Bundle();
        response.putInt(Utils.RESPONSE_CODE, 1);
        Intent intent = new Intent();
        intent.putExtras(response);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
      }
    });
    AlertDialog alertDialog = alert.create();
    alertDialog.show();
  }
}
