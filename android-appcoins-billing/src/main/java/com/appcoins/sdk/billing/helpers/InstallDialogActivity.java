package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import com.appcoins.sdk.android.billing.R;
import com.appcoins.sdk.billing.ConnectToWalletBillingService;

public class InstallDialogActivity extends Activity {

  private int RESULT_USER_CANCELED = 1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.install_dialog_activity);
    WalletUtils.setDialogActivity(this);
    WalletUtils.promptToInstallWallet();
  }

  @Override protected void onResume() {
    super.onResume();
    if (WalletUtils.hasWalletInstalled()) {
      WalletUtils.appcoinsBillingStubHelper.createRepository(new ConnectToWalletBillingService() {
        @Override public void isConnected() {
          Bundle intent =WalletUtils.appcoinsBillingStubHelper.getBuyIntent(WalletUtils.buyItemProperties.getApiVersion(),WalletUtils.buyItemProperties.getPackageName()
          ,WalletUtils.buyItemProperties.getSku(),WalletUtils.buyItemProperties.getType(),WalletUtils.buyItemProperties.getDeveloperPayload());

          PendingIntent pendingIntent = (PendingIntent) intent.getParcelable("BUY_INTENT");
          try {
            startIntentSenderForResult(pendingIntent.getIntentSender(),
                10001, new Intent(), 0, 0, 0);
          } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
          }
        }
      });
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d("Activity Result: ", "onActivityResult(" + requestCode + "," + resultCode + "," + data + ")");
    if (data != null && data.getExtras() != null) {
      Bundle bundle = data.getExtras();
      if (bundle != null) {
        for (String key : bundle.keySet()) {
          Object value = bundle.get(key);
          if (value != null) {
            Log.d("Message Key", key);
            Log.d("Message value", value.toString());
          }
        }
      }
      finishActivity();
    }
  }

  public void finishActivity() {
    Bundle response = new Bundle();
    response.putInt(Utils.RESPONSE_CODE, RESULT_USER_CANCELED);
    Intent intent = new Intent();
    intent.putExtras(response);
    this.setResult(Activity.RESULT_CANCELED, intent);
    this.finish();
  }
}