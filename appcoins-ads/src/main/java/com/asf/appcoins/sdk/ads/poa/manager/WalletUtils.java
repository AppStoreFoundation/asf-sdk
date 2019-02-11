package com.asf.appcoins.sdk.ads.poa.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.asf.appcoins.sdk.ads.BuildConfig;
import com.asf.appcoins.sdk.ads.R;

public class WalletUtils {

  private static String walletPackageName = BuildConfig.BDS_WALLET_PACKAGE_NAME;

  public static boolean hasWalletInstalled(Context context) {
    PackageManager packageManager = context.getPackageManager();

    try {
      packageManager.getPackageInfo(walletPackageName, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }

/*
//duvidas
  public static Single<Boolean> promptToInstallWallet(Activity activity, String message) {
    return showWalletInstallDialog(activity, message).doOnSuccess(aBoolean -> {
      if (aBoolean) {
        gotoStore(activity);
      }
    });
  }
*/

  public static void promptToInstallWallet(Activity activity, String message,
      DialogVisibleListener dialogVisibleListener) {
    showWalletInstallDialog(activity, message, activity, dialogVisibleListener);
  }
/*
  //duvidas
  private static Single<Boolean> showWalletInstallDialog(Context context, String message) {
    return Single.create(emitter -> {
      AlertDialog.Builder builder;
      builder = new AlertDialog.Builder(context);
      builder.setTitle(R.string.wallet_missing)
          .setMessage(message)
          .setPositiveButton(R.string.install, (dialog, which) -> emitter.onSuccess(true))
          .setNegativeButton(R.string.skip, (dialog, which) -> emitter.onSuccess(false))
          .setIcon(android.R.drawable.ic_dialog_alert)
          .show();
    });
  }
*/

  //duvidas
  private static void showWalletInstallDialog(Context context, String message, Activity activity,
      DialogVisibleListener dialogVisibleListener) {

    AlertDialog.Builder builder;
    builder = new AlertDialog.Builder(context);
    builder.setTitle(R.string.wallet_missing);
    builder.setMessage(message);

    builder.setPositiveButton(R.string.install, (dialog, id) -> {
      try {
        activity.startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + walletPackageName)));
      } catch (android.content.ActivityNotFoundException anfe) {
        activity.startActivity(new Intent(Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=" + walletPackageName)));
      }
      dialogVisibleListener.OnDialogVisibleListener(false);
    });

    builder.setNegativeButton(R.string.skip,
        (dialog, id) -> dialogVisibleListener.OnDialogVisibleListener(false));

    builder.setIcon(android.R.drawable.ic_dialog_alert);
    builder.show();
  }

/*
    @NonNull private static void gotoStore(Context activity) {
      try {
        activity.startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + walletPackageName)));
      } catch (android.content.ActivityNotFoundException anfe) {
        activity.startActivity(new Intent(Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=" + walletPackageName)));
      }
    }
*/
/*
  private static void gotoStore(Context activity) {

  }
  */
}
