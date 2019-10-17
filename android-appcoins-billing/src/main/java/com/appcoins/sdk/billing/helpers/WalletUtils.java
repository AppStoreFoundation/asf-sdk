package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.wallet.DialogWalletInstall;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
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

    ArrayList intentServicesResponse = new ArrayList();
    Intent serviceIntent = new Intent(BuildConfig.IAB_BIND_ACTION);

    List<ResolveInfo> intentServices = context.get()
        .getPackageManager()
        .queryIntentServices(serviceIntent, 0);

    if (intentServices.size() > 0 && intentServices != null) {
      for (ResolveInfo intentService : intentServices) {
        intentServicesResponse.add(intentService.serviceInfo.packageName);
      }
      billingPackageName = chooseServiceToBind(intentServicesResponse);
    }
    return billingPackageName != null;
  }

  private static String chooseServiceToBind(ArrayList packageNameServices) {
    String[] packagesOrded = BuildConfig.SERVICE_BIND_LIST.split(",");
    for (int i = 0; i < packagesOrded.length; i++) {
      if (packageNameServices.contains(packagesOrded[i])) {
        return packagesOrded[i];
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
