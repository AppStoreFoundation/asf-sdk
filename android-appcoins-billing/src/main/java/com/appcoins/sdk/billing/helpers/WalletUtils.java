package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import com.appcoins.sdk.android.billing.BuildConfig;
import com.appcoins.sdk.android.billing.R;
import java.lang.ref.WeakReference;

public class WalletUtils {

  private static final String TAG = AppcoinsBillingStubHelper.class.getSimpleName();
  public static String walletPackageName = BuildConfig.BDS_WALLET_PACKAGE_NAME;

  public static WeakReference<Activity> context;

  public static void setContext(Activity cont) {
    context = new WeakReference<>(cont);
  }

  public static boolean hasWalletInstalled() {
    PackageManager packageManager = context.get()
        .getPackageManager();

    try {
      packageManager.getPackageInfo(walletPackageName, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }

  public static void promptToInstallWallet() {
    final Activity act;
    try {
      act = getActivity();
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    if (act == null) {
      return;
    }
    AlertDialog.Builder builder;
    builder = new AlertDialog.Builder(act);
    builder.setTitle(R.string.wallet_missing);
    builder.setMessage(act.getString(R.string.install_wallet_from_iab));
    Log.d("String name: ", "To complete your purchase, you have to install an AppCoins wallet");

    builder.setPositiveButton("INSTALL", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        act.startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + walletPackageName)));
      }
    });

    builder.setNegativeButton("SKIP", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
      }
    });

    builder.setIcon(android.R.drawable.ic_dialog_alert);
    builder.show();
  }

  public static Activity getActivity() {
    return context.get();
  }
}
