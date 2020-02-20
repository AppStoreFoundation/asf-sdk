package com.appcoins.sdk.billing.payasguest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
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

import static com.appcoins.sdk.billing.helpers.InstallDialogActivity.ERROR_RESULT_CODE;
import static com.appcoins.sdk.billing.helpers.Utils.RESPONSE_CODE;

public class IabActivity extends Activity implements IabView {

  public final static int LAUNCH_BILLING_FLOW_REQUEST_CODE = 10001;
  public final static String PAYMENT_METHOD_KEY = "payment_method";
  public final static String WALLET_ADDRESS_KEY = "wallet_address_key";
  public final static String EWT_KEY = "ewt_key";
  public final static String FIAT_VALUE_KEY = "fiat_value";
  public final static String FIAT_CURRENCY_KEY = "fiat_currency";
  public final static String APPC_VALUE_KEY = "appc_value";
  public final static String SKU_KEY = "sku_key";
  private final static String TRANSLATIONS = "translations";
  private TranslationsModel translationsModel;
  private FrameLayout frameLayout;
  private BuyItemProperties buyItemProperties;

  @SuppressLint("ResourceType") @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //This log is necessary for the automatic test that validates the wallet installation dialog
    Log.d("InstallDialog", "com.appcoins.sdk.billing.helpers.InstallDialogActivity started");

    int backgroundColor = Color.parseColor("#64000000");
    frameLayout = new FrameLayout(this);
    frameLayout.setId(3);
    frameLayout.setBackgroundColor(backgroundColor);

    setContentView(frameLayout);

    buyItemProperties = (BuyItemProperties) getIntent().getSerializableExtra(
        AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES);

    if (savedInstanceState != null) {
      translationsModel = (TranslationsModel) savedInstanceState.get(TRANSLATIONS);
    } else {
      fetchTranslations();
      PaymentMethodsFragment paymentMethodsFragment = new PaymentMethodsFragment();
      Bundle bundle = new Bundle();
      bundle.putSerializable(AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES, buyItemProperties);
      paymentMethodsFragment.setArguments(bundle);
      navigateTo(paymentMethodsFragment);
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == LAUNCH_BILLING_FLOW_REQUEST_CODE) {
      setResult(resultCode, data);
      finish();
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

  @SuppressLint("ResourceType") private void navigateTo(Fragment fragment) {
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

  @Override public void finishWithError() {
    Intent response = new Intent();
    response.putExtra("RESPONSE_CODE", ERROR_RESULT_CODE);
    setResult(ERROR_RESULT_CODE, response);
    finish();
  }

  @Override public void showAlertNoBrowserAndStores() {
    buildAlertNoBrowserAndStores();
  }

  @Override public void redirectToWalletInstallation(Intent intent) {
    startActivity(intent);
  }

  @Override
  public void navigateToAdyen(String selectedRadioButton, String walletAddress, String ewt,
      String fiatPrice, String fiatPriceCurrencyCode, String appcPrice, String sku) {
    AdyenPaymentFragment adyenPaymentFragment = new AdyenPaymentFragment();
    Bundle bundle = new Bundle();
    bundle.putString(PAYMENT_METHOD_KEY, selectedRadioButton);
    bundle.putString(WALLET_ADDRESS_KEY, walletAddress);
    bundle.putString(EWT_KEY, ewt);
    bundle.putString(FIAT_VALUE_KEY, fiatPrice);
    bundle.putString(FIAT_CURRENCY_KEY, fiatPriceCurrencyCode);
    bundle.putString(APPC_VALUE_KEY, appcPrice);
    bundle.putString(SKU_KEY, sku);
    bundle.putSerializable(AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES, buyItemProperties);
    adyenPaymentFragment.setArguments(bundle);
    navigateTo(adyenPaymentFragment);
  }

  @Override public void startIntentSenderForResult(IntentSender intentSender, int requestCode) {
    try {
      startIntentSenderForResult(intentSender, requestCode, new Intent(), 0, 0, 0);
    } catch (IntentSender.SendIntentException e) {
      finishWithError();
    }
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
