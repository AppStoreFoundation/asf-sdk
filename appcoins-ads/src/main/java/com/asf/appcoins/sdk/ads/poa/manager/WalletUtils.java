package com.asf.appcoins.sdk.ads.poa.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.asf.appcoins.sdk.ads.BuildConfig;
import com.asf.appcoins.sdk.ads.R;

public class WalletUtils implements DialogInterface.OnClickListener {

  private static String walletPackageName = BuildConfig.BDS_WALLET_PACKAGE_NAME;
  private Context context;
  private Activity activity;
  private DialogVisibleListener dialogVisibleListener;

  public WalletUtils(Context context,Activity activity,DialogVisibleListener dialogVisibleListener){
    this.context = context;
    this.activity = activity;
    this.dialogVisibleListener = dialogVisibleListener;
  }


  public boolean hasWalletInstalled() {
    PackageManager packageManager = context.getPackageManager();

    try {
      packageManager.getPackageInfo(walletPackageName, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }

  public void promptToInstallWallet(String message) {
    showWalletInstallDialog(message);
  }

  private void showWalletInstallDialog( String message) {

    AlertDialog.Builder builder;
    builder = new AlertDialog.Builder(context);
    builder.setTitle(R.string.wallet_missing);
    builder.setMessage(message);
    builder.setPositiveButton(R.string.install,this );
    builder.setIcon(android.R.drawable.ic_dialog_alert);
    builder.show();
  }

  @Override public void onClick(DialogInterface dialogInterface, int i) {
    try {
      if (DialogInterface.BUTTON_POSITIVE == i) {
        activity.startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + walletPackageName)));
      } else {
        dialogVisibleListener.OnDialogVisibleListener(false);
      }
    } catch (android.content.ActivityNotFoundException anfe) {
      activity.startActivity(new Intent(Intent.ACTION_VIEW,
          Uri.parse("https://play.google.com/store/apps/details?id=" + walletPackageName)));
      dialogVisibleListener.OnDialogVisibleListener(false);
    }
  }
}
