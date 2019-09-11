package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import com.appcoins.sdk.android.billing.BuildConfig;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.wallet.DialogWalletInstall;
import java.lang.ref.WeakReference;

public class WalletUtils {

  public static WeakReference<Activity> context;
  public static Activity activity;
  public static AppcoinsBillingStubHelper appcoinsBillingStubHelper;
  public static BuyItemProperties buyItemProperties;

  public static void setContext(Activity cont) {
    context = new WeakReference<>(cont);
  }

  public static void setDialogActivity(Activity act) {
    activity = act;
  }

  public static void setAppcoinsBillingStubHelper(AppcoinsBillingStubHelper helper) {
    appcoinsBillingStubHelper = helper;
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
