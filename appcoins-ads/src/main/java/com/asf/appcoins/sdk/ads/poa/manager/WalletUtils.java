package com.asf.appcoins.sdk.ads.poa.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import com.asf.appcoins.sdk.ads.BuildConfig;

public class WalletUtils {

  public static String walletPackageName = BuildConfig.BDS_WALLET_PACKAGE_NAME;

  public static String aptoidePackageName = BuildConfig.APTOIDE_PACKAGE_NAME;

  public static Context context;

  public static PendingIntent pendingIntent;

  public static void setContext(Context cont) {
    context = cont;
  }

  public static boolean hasAptoideInstalled() {
    PackageManager packageManager = context.getPackageManager();

    try {
      packageManager.getPackageInfo(aptoidePackageName, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }

  public static boolean hasWalletInstalled() {
    PackageManager packageManager = context.getPackageManager();

    try {
      packageManager.getPackageInfo(walletPackageName, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }

  public static void createInstallWalletNotification() {
    Intent intent = null;
    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + walletPackageName));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    if (hasAptoideInstalled()) {
      intent.setPackage(aptoidePackageName);
    }

    PackageManager packageManager = context.getPackageManager();
    ApplicationInfo applicationInfo = null;
    Resources resources = null;

    try {
      applicationInfo =
          packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      resources = packageManager.getResourcesForApplication(applicationInfo);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      Log.d(WalletUtils.class.getName(), "ApplicationInfo not Found.");
      return;
    }

    int applicationIconResID = applicationInfo.icon;

    String iconName = resources.getResourceName(applicationIconResID);
    String typeName = resources.getResourceTypeName(applicationIconResID);
    String packageName = resources.getResourcePackageName(applicationIconResID);
    int identifier = resources.getIdentifier(iconName, typeName, packageName);

    pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

    Notification notification = new Notification.Builder(context).setSmallIcon(identifier)
        .setContentTitle("APPC Wallet Missing")
        .setContentText(
            "To get your reward in AppCoins, you need to install the AppCoins BDS Wallet.")
        .setAutoCancel(true)
        .addAction(identifier, "Install", pendingIntent)
        .build();

    NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0, notification);
  }
}
