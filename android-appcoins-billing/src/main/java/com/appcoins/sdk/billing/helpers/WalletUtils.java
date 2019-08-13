package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.appcoins.sdk.android.billing.BuildConfig;
import com.appcoins.sdk.billing.wallet.DialogWalletInstall;
import java.lang.ref.WeakReference;

public class WalletUtils {

  public static WeakReference<Activity> context;
  public static Activity activity;

  public static void setContext(Activity cont) {
    context = new WeakReference<>(cont);
  }

  public static void setDialogActivity(Activity act) {
    activity = act;
  }

  public static boolean hasWalletInstalled() {
    PackageManager packageManager = context.get()
        .getPackageManager();

    try {
      packageManager.getPackageInfo(BuildConfig.BDS_WALLET_PACKAGE_NAME, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }

  public static int getAptoideVersion() {

    final PackageInfo pInfo;
    int versionCode = 0;

    try {
      pInfo = context.get()
          .getPackageManager()
          .getPackageInfo(BuildConfig.APTOIDE_PACKAGE_NAME, 0);
      String versionName = pInfo.versionName;

      //VersionCode is deprecated for api 28
      if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        versionCode = (int) pInfo.getLongVersionCode();
      } else {
        //noinspection deprecation
        versionCode = pInfo.versionCode;
      }

      Log.d("AptoideVersion", "Version Code is: " + versionCode);
      Log.d("AptoideVersion", "Version Name is: " + versionName);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return versionCode;
  }

  public static boolean hasAptoideInstalled() {

    PackageManager packageManager = context.get()
        .getPackageManager();
    try {
      return packageManager.getApplicationInfo(BuildConfig.APTOIDE_PACKAGE_NAME, 0).enabled;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }

  public static void promptToInstallWallet() {
    final Activity act;
    act = context.get();

    if (act == null) {
      return;
    }

    DialogWalletInstall.with(activity)
        .show();
  }

  public static Activity getActivity() {
    return context.get();
  }
}
