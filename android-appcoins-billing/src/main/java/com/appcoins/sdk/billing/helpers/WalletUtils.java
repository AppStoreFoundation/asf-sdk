package com.appcoins.sdk.billing.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Build;
import com.appcoins.billing.sdk.BuildConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.appcoins.sdk.billing.helpers.CafeBazaarUtils.getUserCountry;
import static com.appcoins.sdk.billing.helpers.CafeBazaarUtils.userFromIran;

public class WalletUtils {

  private static final int UNINSTALLED_APTOIDE_VERSION_CODE = 0;
  private static final int UNKNOWN_ERROR_CODE = 600;

  public static Context context;
  private static String billingPackageName;
  private static String iabAction;
  private static boolean cafeBazaarWalletAvailable = true;

  public static boolean hasWalletInstalled() {
    if (billingPackageName == null) {
      getPackageToBind();
    }
    return billingPackageName != null;
  }

  private static void getPackageToBind() {
    List<String> intentServicesResponse = new ArrayList<>();
    iabAction = BuildConfig.IAB_BIND_ACTION;
    if ((isAppInstalled(BuildConfig.CAFE_BAZAAR_PACKAGE_NAME, context.getPackageManager())
        || userFromIran(getUserCountry(context))) && cafeBazaarWalletAvailable) {
      checkForBazaarWalletAvailability();
    }
    Intent serviceIntent = new Intent(iabAction);

    List<ResolveInfo> intentServices = context.getPackageManager()
        .queryIntentServices(serviceIntent, 0);

    if (intentServices != null && intentServices.size() > 0) {
      for (ResolveInfo intentService : intentServices) {
        intentServicesResponse.add(intentService.serviceInfo.packageName);
      }
      billingPackageName = chooseServiceToBind(intentServicesResponse, iabAction);
    }
  }

  private static String chooseServiceToBind(List<String> packageNameServices, String action) {
    if (action.equals(BuildConfig.CB_IAB_BIND_ACTION)) {
      if (packageNameServices.contains(BuildConfig.CAFE_BAZAAR_WALLET_PACKAGE_NAME)) {
        return BuildConfig.CAFE_BAZAAR_WALLET_PACKAGE_NAME;
      }
      return null;
    } else {
      String[] packagesOrdered = BuildConfig.SERVICE_BIND_LIST.split(",");
      for (String address : packagesOrdered) {
        if (packageNameServices.contains(address)) {
          return address;
        }
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

  public static String getIabAction() {
    if (iabAction == null) {
      iabAction = BuildConfig.IAB_BIND_ACTION;
    }
    return iabAction;
  }

  private static void checkForBazaarWalletAvailability() {
    final CountDownLatch latch = new CountDownLatch(1);
    ResponseListener responseListener = new ResponseListener() {
      @Override public void onResponseCode(int code) {
        cafeBazaarWalletAvailable = code < 300 || code == UNKNOWN_ERROR_CODE;
        latch.countDown();
      }
    };
    AsyncTask asyncTask = new CafeBazaarResponseAsync(responseListener);
    asyncTask.execute();
    try {
      latch.await(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      cafeBazaarWalletAvailable = false;
      e.printStackTrace();
    }
    if (cafeBazaarWalletAvailable) {
      iabAction = BuildConfig.CB_IAB_BIND_ACTION;
    }
  }

  public static boolean isCafeBazaarWalletAvailable() {
    return cafeBazaarWalletAvailable;
  }
}
