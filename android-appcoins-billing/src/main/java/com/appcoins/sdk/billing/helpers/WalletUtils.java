package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import com.appcoins.sdk.android.billing.BuildConfig;
import java.lang.ref.WeakReference;

public class WalletUtils {

  private static final String TAG = AppcoinsBillingStubHelper.class.getSimpleName();
  public static String walletPackageName = BuildConfig.BDS_WALLET_PACKAGE_NAME;
  public static String aptoidePackageName = BuildConfig.APTOIDE_PACKAGE_NAME;

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

  public static boolean hasAptoideInstalled() {

    PackageManager packageManager = context.get()
        .getPackageManager();
    try {
      return packageManager.getApplicationInfo(aptoidePackageName, 0).enabled;
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

    final Intent appStoreIntent =
        new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + walletPackageName));

    AlertDialog.Builder builder;
    builder = new AlertDialog.Builder(act);
    builder.setTitle("APPC Wallet Missing");
    builder.setMessage("To complete your purchase, you have to install an AppCoins wallet");
    Log.d("String name: ", "To complete your purchase, you have to install an AppCoins wallet");

    builder.setPositiveButton("INSTALL", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        OnInstallClickAction(act, appStoreIntent);
      }
    });

    builder.setNegativeButton("SKIP", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
      }
    });

    builder.setIcon(android.R.drawable.ic_dialog_alert);
    Log.d(TAG, "promptToInstallWallet: builder.show()");
    builder.show();
  }

  private static void OnInstallClickAction(Activity act, Intent appStoreIntent) {

    //Check if the user has aptoide installed and open the aptoide's wallet page
    if (hasAptoideInstalled()) {
      Log.d("AptoideInstallation", "Aptoide is installed on this device");
      appStoreIntent.setPackage(aptoidePackageName);
    } else {
      //Open google play's wallet page instead
      Log.d("AptoideInstallation", "Aptoide is not installed on this device");
    }

    try {
      act.startActivity(appStoreIntent);
    } catch (android.content.ActivityNotFoundException exception) {
      act.startActivity(new Intent(Intent.ACTION_VIEW,
          Uri.parse("https://play.google.com/store/apps/details?id=" + walletPackageName)));
    }
  }

  public static Activity getActivity() {
    return context.get();
  }
}
