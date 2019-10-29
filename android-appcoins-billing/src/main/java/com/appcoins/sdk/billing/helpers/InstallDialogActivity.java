package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.StartPurchaseAfterBindListener;

public class InstallDialogActivity extends Activity {

  public final static String KEY_BUY_INTENT = "BUY_INTENT";
  public final static String INSTALL_DIALOG_ACTIVITY = "install_dialog_activity";
  public final static String LOADING_DIALOG_CARD = "loading_dialog_install";
  public final static int REQUEST_CODE = 10001;
  public AppcoinsBillingStubHelper appcoinsBillingStubHelper;
  public BuyItemProperties buyItemProperties;
  private View loadingDialogInstall;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(this.getResources()
        .getIdentifier(INSTALL_DIALOG_ACTIVITY, "layout", this.getPackageName()));

    appcoinsBillingStubHelper = AppcoinsBillingStubHelper.getInstance();
    buyItemProperties = (BuyItemProperties) getIntent().getSerializableExtra(
        AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES);

    WalletUtils.setDialogActivity(this);
    WalletUtils.promptToInstallWallet();
  }

  @Override protected void onResume() {
    super.onResume();
    if (WalletUtils.hasWalletInstalled()) {
      showLoadingDialog();
      loadingDialogInstall.setVisibility(View.VISIBLE);
      WalletUtils.dismissDialogWalletInstall();
      appcoinsBillingStubHelper.createRepository(new StartPurchaseAfterBindListener() {
        @Override public void startPurchaseAfterBind() {
          makeTheStoredPurchase();
        }
      });
    }
  }

  private void showLoadingDialog() {
    this.setContentView(this.getResources()
        .getIdentifier(LOADING_DIALOG_CARD, "layout", this.getPackageName()));
    loadingDialogInstall = this.findViewById(this.getResources()
        .getIdentifier(LOADING_DIALOG_CARD, "id", this.getPackageName()));
    loadingDialogInstall.setVisibility(View.VISIBLE);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    finishActivity(resultCode, data);
  }

  public void makeTheStoredPurchase() {
    Bundle intent = appcoinsBillingStubHelper.getBuyIntent(buyItemProperties.getApiVersion(),
        buyItemProperties.getPackageName(), buyItemProperties.getSku(), buyItemProperties.getType(),
        buyItemProperties.getDeveloperPayload());

    PendingIntent pendingIntent = intent.getParcelable(KEY_BUY_INTENT);
    try {
      loadingDialogInstall.setVisibility(View.INVISIBLE);
      startIntentSenderForResult(pendingIntent.getIntentSender(), REQUEST_CODE, new Intent(), 0, 0,
          0);
    } catch (IntentSender.SendIntentException e) {
      e.printStackTrace();
    }
  }

  private void finishActivity(int resultCode, Intent data) {
    this.setResult(resultCode, data);
    this.finish();
  }
}