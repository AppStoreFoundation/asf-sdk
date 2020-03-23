package com.appcoins.sdk.billing.payasguest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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
import com.appcoins.sdk.billing.helpers.InstallDialogActivity;
import com.appcoins.sdk.billing.helpers.Utils;
import com.appcoins.sdk.billing.helpers.translations.TranslationsRepository;
import com.appcoins.sdk.billing.listeners.payasguest.ActivityResultListener;

import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES;
import static com.appcoins.sdk.billing.helpers.InstallDialogActivity.ERROR_RESULT_CODE;
import static com.appcoins.sdk.billing.helpers.Utils.RESPONSE_CODE;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iap_wallet_and_appstore_not_installed_popup_body;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iap_wallet_and_appstore_not_installed_popup_button;
import static com.appcoins.sdk.billing.utils.LayoutUtils.generateRandomId;

public class IabActivity extends Activity implements IabView {

  public final static int LAUNCH_INSTALL_BILLING_FLOW_REQUEST_CODE = 10001;
  private final static int WEB_VIEW_REQUEST_CODE = 1234;
  private static int IAB_ACTIVITY_ID;
  private TranslationsRepository translations;
  private FrameLayout frameLayout;
  private BuyItemProperties buyItemProperties;
  private ActivityResultListener activityResultListener;
  private boolean backEnabled = true;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //This log is necessary for the automatic test that validates the wallet installation dialog
    Log.d("InstallDialog", "com.appcoins.sdk.billing.helpers.InstallDialogActivity started");

    int backgroundColor = Color.parseColor("#64000000");
    frameLayout = new FrameLayout(this);
    if (savedInstanceState == null) {
      IAB_ACTIVITY_ID = generateRandomId();
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
      if (resultCode == WebViewActivity.SUCCESS) {
        if (activityResultListener != null) {
          activityResultListener.onActivityResult(data.getData(),
              data.getStringExtra(WebViewActivity.TRANSACTION_ID));
        } else {
          Log.w("IabActivity", "ActivityResultListener was not set");
          close(true);
        }
      } else {
        close(true);
      }
    }
  }

  private void navigateTo(Fragment fragment) {
    getFragmentManager().beginTransaction()
        .replace(frameLayout.getId(), fragment)
        .commit();
  }

  @Override public void close(boolean withError) {
    Bundle bundle = new Bundle();
    if (withError) {
      bundle.putInt(RESPONSE_CODE, 6); //ERROR
    } else {
      bundle.putInt(RESPONSE_CODE, 1); //CANCEL
    }
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
  public void navigateToAdyen(String selectedRadioButton, String walletAddress, String signature,
      String fiatPrice, String fiatPriceCurrencyCode, String appcPrice, String sku) {
    AdyenPaymentFragment adyenPaymentFragment =
        AdyenPaymentFragment.newInstance(selectedRadioButton, walletAddress, signature, fiatPrice,
            fiatPriceCurrencyCode, appcPrice, sku, buyItemProperties);
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
    Intent intent = new Intent(this.getApplicationContext(), InstallDialogActivity.class);
    intent.putExtra(BUY_ITEM_PROPERTIES, buyItemProperties);
    finish();
    startActivity(intent);
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
    String body = "Package Name: "
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
    intent.putExtra(Intent.EXTRA_SUBJECT, emailInfo.getTitle());
    intent.putExtra(Intent.EXTRA_EMAIL, extraEmail);
    intent.putExtra(Intent.EXTRA_TEXT, body);
    PackageManager packageManager = getPackageManager();
    ActivityInfo activityInfo = intent.resolveActivityInfo(packageManager, 0);
    if (activityInfo != null) {
      startActivity(Intent.createChooser(intent, "Select email application."));
    }
  }

  @Override public boolean hasEmailApplication() {
    PackageManager packageManager = getPackageManager();
    Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
    intent.setType("message/rfc822");
    ActivityInfo activityInfo = intent.resolveActivityInfo(packageManager, 0);
    return activityInfo != null;
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
