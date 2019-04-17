package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.content.DialogInterface;
import com.appcoins.sdk.android_appcoins_billing.BuildConfig;
import com.appcoins.sdk.android_appcoins_billing.R;

public class WalletUtils {

  private static String walletPackageName = BuildConfig.BDS_WALLET_PACKAGE_NAME;

  public static boolean hasWalletInstalled(Context context) {
    PackageManager packageManager = context.getPackageManager();

    try {
      packageManager.getPackageInfo(walletPackageName, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return false;
    }
  }
}
