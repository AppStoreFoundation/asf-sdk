package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import com.appcoins.billing.sdk.R;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.ConnectToWalletBillingService;

public class InstallDialogActivity extends Activity {

  public final static String INSTALL_DIALOG_ACTIVITY = "install_dialog_activity";
  public final static String KEY_BUY_INTENT = "BUY_INTENT";
  public final static int REQUEST_CODE = 10001;
  public AppcoinsBillingStubHelper appcoinsBillingStubHelper;
  public BuyItemProperties buyItemProperties;
  private View walletCreationCard;
  private View walletCreationText;

  @Override protected void onCreate(Bundle savedInstanceState) {
    appcoinsBillingStubHelper = AppcoinsBillingStubHelper.getInstance();
    buyItemProperties = (BuyItemProperties) getIntent().getSerializableExtra(
        AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES);
    super.onCreate(savedInstanceState);
    setContentView(this.getResources()
        .getIdentifier(INSTALL_DIALOG_ACTIVITY, "layout", this.getPackageName()));
    WalletUtils.setDialogActivity(this);
    WalletUtils.promptToInstallWallet();
    setContentView(R.layout.activity_iab_wallet_creation);
    walletCreationCard = findViewById(R.id.create_wallet_card);
    walletCreationText = findViewById(R.id.create_wallet_text);
    walletCreationCard.setVisibility(View.INVISIBLE);
    walletCreationText.setVisibility(View.INVISIBLE);
  }

  @Override protected void onResume() {
    super.onResume();
    if (WalletUtils.hasWalletInstalled()) {
      walletCreationCard.setVisibility(View.VISIBLE);
      walletCreationText.setVisibility(View.VISIBLE);

      appcoinsBillingStubHelper.createRepository(new ConnectToWalletBillingService() {
        @Override public void isConnected() {
          makeTheStoredPurchase();
        }
      });
    }
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
      walletCreationCard.setVisibility(View.INVISIBLE);
      walletCreationText.setVisibility(View.INVISIBLE);

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