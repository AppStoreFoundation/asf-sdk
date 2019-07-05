package com.asf.appcoins.sdk.ads.poa.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.asf.appcoins.sdk.ads.BuildConfig;
import com.asf.appcoins.sdk.ads.R;

public class WalletUtils {

  public static String walletPackageName = BuildConfig.BDS_WALLET_PACKAGE_NAME;

  public static String aptoidePackageName = BuildConfig.APTOIDE_PACKAGE_NAME;

  private static String POA_NOTIFICATION_HEADS_UP = "POA_NOTIFICATION_HEADS_UP";

  private static String POA_NOTIFICATION_NORMAL = "POA_NOTIFICATION_NORMAL";

  private static String URL_INTENT_INSTALL =
      "market://details?id=com.appcoins.wallet&utm_source=appcoinssdk&app_source=";

  private static String URL_APTOIDE_PARAMETERS = "&utm_source=appcoinssdk&app_source=";

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

    NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    Intent intent = getNotificationIntent();

    if (intent == null) {
      return;
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

      createNotificationChannels("0", POA_NOTIFICATION_HEADS_UP, POA_NOTIFICATION_HEADS_UP,
          notificationManager, NotificationManager.IMPORTANCE_HIGH);

      createNotificationChannels("1", POA_NOTIFICATION_NORMAL, POA_NOTIFICATION_NORMAL,
          notificationManager, NotificationManager.IMPORTANCE_DEFAULT);

      Notification notificationHeadsUp = buildNotification("0", intent, true);
      Notification notificationNormal = buildNotification("1", intent, false);

      notificationManager.notify(1, notificationNormal);
      notificationManager.notify(0, notificationHeadsUp);
    } else {

      Notification notificationHeadsUp = buildNotification("0", intent, true);

      notificationManager.notify(0, notificationHeadsUp);
    }
  }

  private static void createNotificationChannels(String id, String name, String description,
      NotificationManager notificationManager, int priority) {
    NotificationChannel channelHeadUp = new NotificationChannel(id, name, priority);
    channelHeadUp.setDescription(description);
    notificationManager.createNotificationChannel(channelHeadUp);
  }

  private static Intent getNotificationIntent() {
    String url = URL_INTENT_INSTALL;

    if (hasAptoideInstalled()) {
      url += URL_APTOIDE_PARAMETERS + context.getPackageName();
    }


    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    PackageManager packageManager = context.getPackageManager();
    ApplicationInfo applicationInfo = null;
    Resources resources = null;

    try {
      applicationInfo =
          packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      resources = packageManager.getResourcesForApplication(applicationInfo);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      Log.d(WalletUtils.class.getName(), "Not found Application Info");
      return intent;
    }

    int applicationIconResID = applicationInfo.icon;

    String iconName = resources.getResourceName(applicationIconResID);
    String typeName = resources.getResourceTypeName(applicationIconResID);
    String packageName = resources.getResourcePackageName(applicationIconResID);
    int identifier = resources.getIdentifier(iconName, typeName, packageName);

    intent.putExtra("identifier", identifier);

    return intent;
  }

  private static Notification buildNotification(String channelId, Intent intent,
      boolean useTimeOut) {

    Notification.Builder builder;
    pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      builder = new Notification.Builder(context, channelId);
      builder.setFullScreenIntent(pendingIntent, true);
      if (useTimeOut) {
        builder.setTimeoutAfter(3000);
      }
    } else {
      builder = new Notification.Builder(context);
      builder.setPriority(Notification.PRIORITY_MAX);
      builder.setContentIntent(pendingIntent);
      builder.setVibrate(new long[0]);
    }

    try{
      builder.setSmallIcon(intent.getExtras()
          .getInt("identifier"))
          .setContentTitle(context.getString(R.string.poa_wallet_not_installed_notification_title))
          .setContentText(context.getString(R.string.poa_wallet_not_installed_notification_body));
    }catch(NullPointerException e){
      e.printStackTrace();
    }

    Notification notification = builder.build();

    return notification;
  }

}
