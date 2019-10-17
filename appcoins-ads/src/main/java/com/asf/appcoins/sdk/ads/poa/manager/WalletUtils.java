package com.asf.appcoins.sdk.ads.poa.manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.asf.appcoins.sdk.ads.BuildConfig;
import java.util.List;
import java.util.ArrayList;


public class WalletUtils {

  private static String POA_NOTIFICATION_HEADS_UP = "POA_NOTIFICATION_HEADS_UP";

  private static int POA_NOTIFICATION_ID = 0;

  private static String URL_INTENT_INSTALL =
      "market://details?id=com.appcoins.wallet&utm_source=appcoinssdk&app_source=";

  private static String URL_APTOIDE_PARAMETERS = "&utm_source=appcoinssdk&app_source=";

  private static PendingIntent pendingIntent;

  private static NotificationManager notificationManager;

  private static boolean hasPopup;

  private static String POA_WALLET_NOT_INSTALLED_NOTIFICATION_TITLE =
      "poa_wallet_not_installed_notification_title";
  private static String POA_WALLET_NOT_INSTALLED_NOTIFICATION_BODY =
      "poa_wallet_not_installed_notification_body";

  public static Context context;

  public static String billingPackageName;

  public static void setContext(Context cont) {
    context = cont;
  }

  public static boolean hasAptoideInstalled() {
    PackageManager packageManager = context.getPackageManager();

    try {
      packageManager.getPackageInfo(BuildConfig.APTOIDE_PACKAGE_NAME, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }

  public static boolean hasWalletInstalled() {
    ArrayList <String> intentServicesResponse = new ArrayList<String>();
    Intent serviceIntent = new Intent(BuildConfig.ADVERTISEMENT_BIND_ACTION);

    List<ResolveInfo> intentServices = context
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

  public static String getBillingServicePackageName() {
    return billingPackageName;
  }

  public static void removeNotification() {
    if (hasPopup) {
      notificationManager.cancel(POA_NOTIFICATION_ID);
      hasPopup = false;
    }
  }

  public static void createInstallWalletNotification() {

    Intent intent = getNotificationIntent();

    if (intent == null) {
      return;
    }

    notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    hasPopup = true;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

      NotificationChannel channelHeadUp =
          new NotificationChannel(Integer.toString(POA_NOTIFICATION_ID), POA_NOTIFICATION_HEADS_UP,
              NotificationManager.IMPORTANCE_HIGH);
      channelHeadUp.setImportance(NotificationManager.IMPORTANCE_HIGH);
      channelHeadUp.setDescription(POA_NOTIFICATION_HEADS_UP);
      channelHeadUp.setVibrationPattern(new long[0]);
      channelHeadUp.setShowBadge(true);
      channelHeadUp.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
      notificationManager.createNotificationChannel(channelHeadUp);
      notificationManager.notify(0,
          buildNotification(Integer.toString(POA_NOTIFICATION_ID), intent));
    } else {
      Notification notificationHeadsUp = buildNotificationOlderVersion(intent);
      notificationManager.notify(POA_NOTIFICATION_ID, notificationHeadsUp);
    }
  }

  private static Intent getNotificationIntent() {
    String url = URL_INTENT_INSTALL;
    boolean hasAptoide = hasAptoideInstalled();

    if (hasAptoide) {
      url += URL_APTOIDE_PARAMETERS + context.getPackageName();
    }

    Intent intent = null;
    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    if (hasAptoide) {
      intent.setPackage(BuildConfig.APTOIDE_PACKAGE_NAME);
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

  private static Notification buildNotification(String channelId, Intent intent) {
    Notification.Builder builder;
    pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    builder = new Notification.Builder(context, channelId);
    builder.setContentIntent(pendingIntent);
    try {
      Resources resources = context.getResources();
      int titleId = resources.getIdentifier(POA_WALLET_NOT_INSTALLED_NOTIFICATION_TITLE, "string",
          context.getPackageName());
      int bodyId = resources.getIdentifier(POA_WALLET_NOT_INSTALLED_NOTIFICATION_BODY, "string",
          context.getPackageName());

      builder.setSmallIcon(intent.getExtras()
          .getInt("identifier"))
          .setAutoCancel(true)
          .setContentTitle(context.getString(titleId))
          .setContentText(context.getString(bodyId));
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return builder.build();
  }

  private static Notification buildNotificationOlderVersion(Intent intent) {

    Notification.Builder builder;
    pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

    builder = new Notification.Builder(context);
    builder.setPriority(Notification.PRIORITY_MAX);
    builder.setContentIntent(pendingIntent);
    builder.setVibrate(new long[0]);

    try {
      Resources resources = context.getResources();
      int titleId = resources.getIdentifier(POA_WALLET_NOT_INSTALLED_NOTIFICATION_TITLE, "string",
          context.getPackageName());
      int bodyId = resources.getIdentifier(POA_WALLET_NOT_INSTALLED_NOTIFICATION_BODY, "string",
          context.getPackageName());

      builder.setSmallIcon(intent.getExtras()
          .getInt("identifier"))
          .setAutoCancel(true)
          .setContentTitle(context.getString(titleId))
          .setContentText(context.getString(bodyId));
    } catch (NullPointerException e) {
      e.printStackTrace();
    }

    Notification notification = builder.build();

    return notification;
  }
}
