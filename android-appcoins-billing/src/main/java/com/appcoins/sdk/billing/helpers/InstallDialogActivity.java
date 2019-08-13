package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
          finishActivity();
        }
      });
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