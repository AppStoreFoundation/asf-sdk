package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.os.Bundle;
import com.appcoins.sdk.android.billing.R;

public class InstallDialogActivity extends Activity {


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.install_dialog_activity);
    WalletUtils.setDialogActivity(this);
    WalletUtils.promptToInstallWallet();
  }

}