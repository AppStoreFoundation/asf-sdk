package com.asf.appcoins.sdk.core.util.wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.asf.appcoins.sdk.core.BuildConfig;
import com.asf.appcoins.sdk.core.R;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

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

  public static Single<Boolean> promptToInstallWallet(Activity activity, String message) {
    return showWalletInstallDialog(activity, message).doOnSuccess(aBoolean -> {
      if (aBoolean) {
        gotoStore(activity);
      }
    });
  }

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

  @NonNull private static void gotoStore(Context activity) {
    try {
      activity.startActivity(
          new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + walletPackageName)));
    } catch (android.content.ActivityNotFoundException anfe) {
      activity.startActivity(new Intent(Intent.ACTION_VIEW,
          Uri.parse("https://play.google.com/store/apps/details?id=" + walletPackageName)));
    }
  }
}
