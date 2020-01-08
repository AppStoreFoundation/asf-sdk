package com.asf.appcoins.sdk.ads.poa.manager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.asf.appcoins.sdk.ads.BuildConfig;
import java.util.ArrayList;
import java.util.List;

public class WalletUtils {

  private static final String URL_BROWSER = "https://play.google.com/store/apps/details?id="
      + com.appcoins.billing.sdk.BuildConfig.BDS_WALLET_PACKAGE_NAME;
  public static Context context;
  private static String POA_NOTIFICATION_HEADS_UP = "POA_NOTIFICATION_HEADS_UP";
  private static int POA_NOTIFICATION_ID = 0;
  private static int MINIMUM_APTOIDE_VERSION = 9908;
  private static int UNINSTALLED_APTOIDE_VERSION_CODE = 0;
  private static String URL_INTENT_INSTALL = "market://details?id="
      + BuildConfig.BDS_WALLET_PACKAGE_NAME
      + "&utm_source=appcoinssdk&app_source=";
  private static String URL_APTOIDE_PARAMETERS = "&utm_source=appcoinssdk&app_source=";
  private static PendingIntent pendingIntent;
  private static NotificationManager notificationManager;
  private static boolean hasPopup;
  private static String IDENTIFIER_KEY = "identifier";
  private static String POA_WALLET_NOT_INSTALLED_NOTIFICATION_TITLE =
      "poa_wallet_not_installed_notification_title";
  private static String POA_WALLET_NOT_INSTALLED_NOTIFICATION_BODY =
      "poa_wallet_not_installed_notification_body";
  private static String billingPackageName;

  public static void setContext(Context cont) {
    context = cont;
  }

  static boolean hasWalletInstalled() {
    if (billingPackageName == null) {
      getPackageToBind();
    }
    return billingPackageName != null;
  }

  private static void getPackageToBind() {
    List<String> intentServicesResponse = new ArrayList<>();
    Intent serviceIntent = new Intent(com.appcoins.billing.sdk.BuildConfig.IAB_BIND_ACTION);

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
    String[] packagesOrded = BuildConfig.SERVICE_BIND_LIST.split(",");
    for (String address : packagesOrded) {
      if (packageNameServices.contains(address)) {
        return address;
      }
    }
    return null;
  }

  public static String getBillingServicePackageName() {
    if (billingPackageName == null) {
      getPackageToBind();
    }
    return billingPackageName;
  }

  private static int getAptoideVersion() {

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

  private static boolean resolveActivityInfoForIntent(Intent intent) {
    ActivityInfo activityInfo = intent.resolveActivityInfo(context.getPackageManager(), 0);
    return activityInfo == null;
  }

  static void removeNotification() {
    if (hasPopup) {
      notificationManager.cancel(POA_NOTIFICATION_ID);
      hasPopup = false;
    }
  }

  static void createInstallWalletNotification() {

    Intent intent = getNotificationIntentForStore();

    if (resolveActivityInfoForIntent(intent)) {
      intent = getNotificationIntentForBrowser();
      if (resolveActivityInfoForIntent(intent)) {
        intent = null;
      }
    }

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

  private static Intent getNotificationIntentForBrowser() {
    Intent intent;
    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_BROWSER));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    return buildNotification(intent);
  }

  private static Intent getNotificationIntentForStore() {

    String url = URL_INTENT_INSTALL;
    int verCode = WalletUtils.getAptoideVersion();
    if (verCode != UNINSTALLED_APTOIDE_VERSION_CODE) {
      url += URL_APTOIDE_PARAMETERS + context.getPackageName();
    }

    Intent intent;
    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    if (verCode >= MINIMUM_APTOIDE_VERSION) {
      intent.setPackage(BuildConfig.APTOIDE_PACKAGE_NAME);
    }

    return buildNotification(intent);
  }

  private static Intent buildNotification(Intent intent) {
    PackageManager packageManager = context.getPackageManager();
    ApplicationInfo applicationInfo;
    Resources resources;

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

    intent.putExtra(IDENTIFIER_KEY, identifier);

    return intent;
  }

  @SuppressLint("NewApi")
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
          .getInt(IDENTIFIER_KEY))
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
          .getInt(IDENTIFIER_KEY))
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
