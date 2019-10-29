package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.wallet.DialogWalletInstall;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class WalletUtils {

  public static final int UNINSTALLED_APTOIDE_VERSION_CODE = 0;

  public static WeakReference<Activity> context;
  public static Activity activity;
  public static String billingPackageName;
  private static DialogWalletInstall dialogWalletInstall;

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

    if (intentServices != null && intentServices.size() > 0) {
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

  public static int getAptoideVersion() {

    final PackageInfo pInfo;
    int versionCode = UNINSTALLED_APTOIDE_VERSION_CODE;

    try {
      pInfo = context.get()
          .getPackageManager()
          .getPackageInfo(BuildConfig.APTOIDE_PACKAGE_NAME, 0);

      //VersionCode is deprecated for api 28
      if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        versionCode = (int) pInfo.getLongVersionCode();
      } else {
        //noinspection deprecation
        versionCode = pInfo.versionCode;
      }
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return versionCode;
  }

  static void promptToInstallWallet() {
    final Activity act;
    act = context.get();

    if (act == null) {
      return;
    }

    dialogWalletInstall = DialogWalletInstall.with(activity);
    dialogWalletInstall.show();
  }

  static void dismissDialogWalletInstall(){
    dialogWalletInstall.dismiss();
  }

  public static Activity getActivity() {
    return context.get();
  }

  public static String getBillingServicePackageName() {
    return billingPackageName;
  }
}
