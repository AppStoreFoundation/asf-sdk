package com.appcoins.sdk.billing.payasguest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.WebViewActivity;
import com.appcoins.sdk.billing.analytics.AnalyticsManagerProvider;
import com.appcoins.sdk.billing.analytics.BillingAnalytics;
import com.appcoins.sdk.billing.helpers.InstallDialogActivity;
import com.appcoins.sdk.billing.helpers.translations.TranslationsRepository;
import com.appcoins.sdk.billing.listeners.payasguest.ActivityResultListener;

import static com.appcoins.sdk.billing.helpers.Utils.RESPONSE_CODE;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iap_wallet_and_appstore_not_installed_popup_body;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iap_wallet_and_appstore_not_installed_popup_button;
import static com.appcoins.sdk.billing.utils.LayoutUtils.generateRandomId;

public class IabActivity extends Activity implements IabView {

  public final static int LAUNCH_INSTALL_BILLING_FLOW_REQUEST_CODE = 10001;
  public final static String CREDIT_CARD = "credit_card";
  public final static String PAYPAL = "paypal";
  public final static String INSTALL_WALLET = "install_wallet";
  private final static int USER_CANCELED = 1;
  private final static int BILLING_UNAVAILABLE = 3;
  private final static int ERROR = 6;
  private final static int WEB_VIEW_REQUEST_CODE = 1234;
  private final static String FIRST_IMPRESSION_KEY = "first_impression";
  private final static String BUY_ITEM_PROPERTIES = "buy_item_properties";
  private static int IAB_ACTIVITY_ID;
  private TranslationsRepository translations;
  private FrameLayout frameLayout;
  private BuyItemProperties buyItemProperties;
  private ActivityResultListener activityResultListener;
  private boolean backEnabled = true;
  private boolean firstImpression = true;

  public static Intent newIntent(Context context, BuyItemProperties buyItemProperties) {
    Intent intent = new Intent(context, IabActivity.class);
    intent.putExtra(BUY_ITEM_PROPERTIES, buyItemProperties);
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //This log is necessary for the automatic test that validates the wallet installation dialog
    Log.d("InstallDialog", "com.appcoins.sdk.billing.helpers.InstallDialogActivity started");

    int backgroundColor = Color.parseColor("#64000000");
    frameLayout = new FrameLayout(this);
    if (savedInstanceState == null) {
      IAB_ACTIVITY_ID = generateRandomId();
    } else {
      firstImpression = savedInstanceState.getBoolean(FIRST_IMPRESSION_KEY, true);
    }
    frameLayout.setId(IAB_ACTIVITY_ID);
    frameLayout.setBackgroundColor(backgroundColor);

    setContentView(frameLayout);

    buyItemProperties = (BuyItemProperties) getIntent().getSerializableExtra(BUY_ITEM_PROPERTIES);
    translations = TranslationsRepository.getInstance(this);
    if (savedInstanceState == null) {
      navigateToPaymentSelection();
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(FIRST_IMPRESSION_KEY, firstImpression);
  }

  @Override protected void onDestroy() {
    unlockRotation();
    super.onDestroy();
  }

  @Override public void onBackPressed() {
    if (backEnabled) {
      close(false);
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == LAUNCH_INSTALL_BILLING_FLOW_REQUEST_CODE) {
      setResult(resultCode, data);
      finish();
    } else if (requestCode == WEB_VIEW_REQUEST_CODE) {
      if (activityResultListener != null) {
        handleResultCode(resultCode, data);
      } else {
        Log.w("IabActivity", "ActivityResultListener was not set");
        close(true);
      }
    }
  }

  @Override public void close(boolean withError) {
    Bundle bundle = new Bundle();
    if (withError) {
      bundle.putInt(RESPONSE_CODE, ERROR);
    } else {
      bundle.putInt(RESPONSE_CODE, USER_CANCELED);
    }
    Intent intent = new Intent();
    intent.putExtras(bundle);
    setResult(Activity.RESULT_CANCELED, intent);
    finish();
  }

  @Override public void finishWithError() {
    Intent response = new Intent();
    response.putExtra(RESPONSE_CODE, ERROR);
    setResult(ERROR, response);
    finish();
  }

  @Override public void showAlertNoBrowserAndStores() {
    buildAlertNoBrowserAndStores();
  }

  @Override public void redirectToWalletInstallation(Intent intent) {
    startActivity(intent);
  }

  @Override
  public void navigateToAdyen(String paymentMethod, String walletAddress, String signature,
      String fiatPrice, String fiatPriceCurrencyCode, String appcPrice, String sku) {
    AdyenPaymentFragment adyenPaymentFragment =
        AdyenPaymentFragment.newStartTransactionInstance(paymentMethod, walletAddress, signature,
            fiatPrice, fiatPriceCurrencyCode, appcPrice, sku, buyItemProperties);
    navigateTo(adyenPaymentFragment);
  }

  @Override
  public void resumeAdyenTransaction(String paymentMethod, String walletAddress, String signature,
      String fiatPrice, String fiatPriceCurrencyCode, String appcPrice, String sku, String uid) {
    AdyenPaymentFragment adyenPaymentFragment =
        AdyenPaymentFragment.newResumeTransactionInstance(paymentMethod, walletAddress, signature,
            fiatPrice, fiatPriceCurrencyCode, appcPrice, sku, uid, buyItemProperties);
    navigateTo(adyenPaymentFragment);
  }

  @Override public void startIntentSenderForResult(IntentSender intentSender, int requestCode) {
    try {
      startIntentSenderForResult(intentSender, requestCode, new Intent(), 0, 0, 0);
    } catch (IntentSender.SendIntentException e) {
      finishWithError();
    }
  }

  @Override public void lockRotation() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }
  }

  @Override public void unlockRotation() {
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
  }

  @Override public void navigateToUri(String url, String uid) {
    startActivityForResult(WebViewActivity.newIntent(this, url, uid), WEB_VIEW_REQUEST_CODE);
  }

  @Override public void finish(Bundle bundle) {
    setResult(Activity.RESULT_OK, new Intent().putExtras(bundle));
    finish();
  }

  @Override public void navigateToPaymentSelection() {
    navigateTo(PaymentMethodsFragment.newInstance(buyItemProperties));
  }

  @Override public void navigateToInstallDialog() {
    Intent intent =
        InstallDialogActivity.newIntent(this.getApplicationContext(), buyItemProperties);
    finish();
    startActivity(intent);
  }

  @Override public void closeWithBillingUnavailable() {
    Bundle bundle = new Bundle();
    bundle.putInt(RESPONSE_CODE, BILLING_UNAVAILABLE);
    Intent intent = new Intent();
    intent.putExtras(bundle);
    setResult(Activity.RESULT_OK, intent);
    finish();
  }

  @Override public void disableBack() {
    backEnabled = false;
  }

  @Override public void enableBack() {
    backEnabled = true;
  }

  @Override public void setOnActivityResultListener(ActivityResultListener activityResultListener) {
    this.activityResultListener = activityResultListener;
  }

  @Override public void redirectToSupportEmail(EmailInfo emailInfo) {
    String[] extraEmail = new String[] {
        "info@appcoins.io"
    };
    String body =
        "We currently offer support in English language only. Also, please don't delete the "
            + "details below so it's easier for us to look into your issue.\n"
            + "Package Name: "
            + emailInfo.getPackageName()
            + "\n"
            + "SDK version: "
            + emailInfo.getSdkVersionName()
            + "\n"
            + "Item name: "
            + emailInfo.getSku()
            + "\n"
            + "Mobile Version: "
            + emailInfo.getMobileVersion()
            + "\n"
            + "Wallet Address: "
            + emailInfo.getWalletAddress();
    Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
    intent.setType("message/rfc822");
    intent.putExtra(Intent.EXTRA_SUBJECT, "Pay as Guest - " + emailInfo.getAppName());
    intent.putExtra(Intent.EXTRA_EMAIL, extraEmail);
    intent.putExtra(Intent.EXTRA_TEXT, body);
    PackageManager packageManager = getPackageManager();
    ActivityInfo activityInfo = intent.resolveActivityInfo(packageManager, 0);
    if (activityInfo != null) {
      startActivity(Intent.createChooser(intent, ""));
    }
  }

  @Override public boolean hasEmailApplication() {
    PackageManager packageManager = getPackageManager();
    Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
    intent.setType("message/rfc822");
    ActivityInfo activityInfo = intent.resolveActivityInfo(packageManager, 0);
    return activityInfo != null;
  }

  @Override public void sendPurchaseStartEvent(String appcPrice) {
    if (firstImpression) {
      BillingAnalytics billingAnalytics =
          new BillingAnalytics(AnalyticsManagerProvider.provideAnalyticsManager());
      billingAnalytics.sendPurchaseStartEvent(buyItemProperties.getPackageName(),
          buyItemProperties.getSku(), appcPrice, buyItemProperties.getType(),
          BillingAnalytics.START_PAYMENT_METHOD);
      firstImpression = false;
    }
  }

  private void buildAlertNoBrowserAndStores() {
    AlertDialog.Builder alert = new AlertDialog.Builder(this);
    String value = translations.getString(iap_wallet_and_appstore_not_installed_popup_body);
    String dismissValue =
        translations.getString(iap_wallet_and_appstore_not_installed_popup_button);
    alert.setMessage(value);
    alert.setCancelable(true);
    alert.setPositiveButton(dismissValue, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        Bundle response = new Bundle();
        response.putInt(RESPONSE_CODE, 1);
        Intent intent = new Intent();
        intent.putExtras(response);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
      }
    });
    AlertDialog alertDialog = alert.create();
    alertDialog.show();
  }

  private void handleResultCode(int resultCode, Intent data) {
    if (resultCode == WebViewActivity.SUCCESS) {
      activityResultListener.onActivityResult(data.getData(),
          data.getStringExtra(WebViewActivity.TRANSACTION_ID), true);
    } else {
      activityResultListener.onActivityResult(null, "", false);
    }
  }

  private void navigateTo(Fragment fragment) {
    getFragmentManager().beginTransaction()
        .replace(frameLayout.getId(), fragment)
        .commit();
  }
}
