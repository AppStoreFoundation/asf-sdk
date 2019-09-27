package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.wallet.DialogWalletInstall;
import java.lang.ref.WeakReference;
import java.util.List;

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
    Intent serviceIntent = new Intent(BuildConfig.IAB_BIND_ACTION);

    final Context context = WalletUtils.getActivity();

    List<ResolveInfo> intentServices = context.getPackageManager()
        .queryIntentServices(serviceIntent, 0);
    for (ResolveInfo intentService : intentServices) {
      if (intentService.serviceInfo.packageName.equals(BuildConfig.APTOIDE_PACKAGE_NAME_DEV)
          || intentService.resolvePackageName.equals(BuildConfig.BDS_WALLET_PACKAGE_NAME)) {
        return true;
      }
    }
    return false;
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

  public static String getBillingServicePackageName() {
    Intent serviceIntent = new Intent(BuildConfig.IAB_BIND_ACTION);

    final Context context = WalletUtils.getActivity();

    List<ResolveInfo> intentServices = context.getPackageManager()
        .queryIntentServices(serviceIntent, 0);
    for (ResolveInfo intentService : intentServices) {
      if (intentService.serviceInfo.packageName.equals(BuildConfig.APTOIDE_PACKAGE_NAME_DEV)
          || intentService.resolvePackageName.equals(BuildConfig.BDS_WALLET_PACKAGE_NAME)) {
        return intentService.serviceInfo.packageName;
      }
    }
    return null;
  }
}
