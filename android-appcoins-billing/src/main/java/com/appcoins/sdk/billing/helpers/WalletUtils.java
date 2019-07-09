package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import com.appcoins.sdk.android.billing.BuildConfig;
import com.appcoins.sdk.billing.wallet.DialogWalletInstall;
import java.lang.ref.WeakReference;

public class WalletUtils {

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
    act = context.get();

    if (act == null) {
      return;
    }
    /** Here is important to know in advance if the host app has feature graphic,
     *  1- this boolean hasImage is needed to change layout dynamically
     *  2- if so, we need to get  url of this image and then when copy this code to  apk-migrator
     *  as Smali,
     *  the correct dialog_wallet_install_graphic needs to be write  */
    DialogWalletInstall.with(act)
        .show();
  }

  public static Activity getActivity() {
    return context.get();
  }
}
