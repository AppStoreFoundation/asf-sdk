package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
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
  public static String billingPackageName;

  public static void setContext(Activity cont) {
    context = new WeakReference<>(cont);
  }

  public static void setDialogActivity(Activity act) {
    activity = act;
  }

  public static boolean hasWalletInstalled() {
    Intent serviceIntent = new Intent(BuildConfig.IAB_BIND_ACTION);

    List<ResolveInfo> intentServices = context.get()
        .getPackageManager()
        .queryIntentServices(serviceIntent, 0);

    if (intentServices.size() > 0) {
      String[] packageNameArray = new String[intentServices.size()];
      int index = 0;
      for (ResolveInfo intentService : intentServices) {
        packageNameArray[index++] = intentService.serviceInfo.packageName;
      }
      billingPackageName = chooseServiceToBind(packageNameArray);
    }
    return billingPackageName != null;
  }

  private static String chooseServiceToBind(String[] packageNameServices) {
    String[] packagesOrded = BuildConfig.SERVICE_BIND_LIST.split(",");
    for (String packageService : packageNameServices) {
      for (int i = 0; i < packagesOrded.length; i++) {
        if (packageService.equals(packagesOrded[i])) {
          return packageService;
        }
      }
    }
    return null;
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
    return billingPackageName;
  }
}
