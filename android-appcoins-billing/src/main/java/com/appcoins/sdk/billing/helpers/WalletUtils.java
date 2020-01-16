package com.appcoins.sdk.billing.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import com.appcoins.billing.sdk.BuildConfig;
import java.util.ArrayList;
import java.util.List;

public class WalletUtils {

  private static final int UNINSTALLED_APTOIDE_VERSION_CODE = 0;

  public static Context context;
  private static String billingPackageName;

  public static boolean hasWalletInstalled() {
    if (billingPackageName == null) {
      getPackageToBind();
    }
    return billingPackageName != null;
  }

  private static void getPackageToBind() {
    List<String> intentServicesResponse = new ArrayList<>();
    Intent serviceIntent = new Intent(BuildConfig.IAB_BIND_ACTION);

    List<ResolveInfo> intentServices = context.getPackageManager()
        .queryIntentServices(serviceIntent, 0);

    if (intentServices != null && intentServices.size() > 0) {
      for (ResolveInfo intentService : intentServices) {
        intentServicesResponse.add(intentService.serviceInfo.packageName);
      }
      billingPackageName = chooseServiceToBind(intentServicesResponse);
    }
  }

  private static String chooseServiceToBind(List<String> packageNameServices) {
    String[] packagesOrdered = BuildConfig.SERVICE_BIND_LIST.split(",");
    for (String address : packagesOrdered) {
      if (packageNameServices.contains(address)) {
        return address;
      }
    }
    return null;
  }

  static boolean isAppInstalled(String packageName, PackageManager packageManager) {
    try {
      packageManager.getPackageInfo(packageName, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }

  static int getAptoideVersion() {

    final PackageInfo pInfo;
    int versionCode = UNINSTALLED_APTOIDE_VERSION_CODE;

    try {
      pInfo = context.getPackageManager()
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

  public static Context getContext() {
    return context;
  }

  public static void setContext(Context context) {
    WalletUtils.context = context.getApplicationContext();
  }

  public static String getBillingServicePackageName() {
    if (billingPackageName == null) {
      getPackageToBind();
    }
    return billingPackageName;
  }
}
