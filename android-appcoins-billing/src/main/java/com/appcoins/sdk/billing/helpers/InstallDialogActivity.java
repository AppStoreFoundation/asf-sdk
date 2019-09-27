package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.os.Bundle;
import com.appcoins.billing.sdk.R;

public class InstallDialogActivity extends Activity {

  public static String INSTALL_DIALOG_ACTIVITY =  "install_dialog_activity";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(this.getResources()
       .getIdentifier(INSTALL_DIALOG_ACTIVITY, "layout", this.getPackageName()));
    WalletUtils.setDialogActivity(this);
    WalletUtils.promptToInstallWallet();
  }

}