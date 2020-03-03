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
import android.telephony.TelephonyManager;
import android.util.Log;
import com.appcoins.sdk.billing.helpers.TranslationsModel;
import com.appcoins.sdk.billing.helpers.TranslationsXmlParser;
import com.asf.appcoins.sdk.ads.BuildConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WalletUtils {

  private static final String URL_BROWSER = "https://play.google.com/store/apps/details?id="
      + com.appcoins.billing.sdk.BuildConfig.BDS_WALLET_PACKAGE_NAME;
  private static final String CAFE_BAZAAR_APP_URL = "bazaar://details?id=com.hezardastan.wallet";
  private static final String CAFE_BAZAAR_WEB_URL =
      "https://cafebazaar.ir/app/com.hezardastaan.wallet";
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
  private static String billingPackageName;
  private static String iabAction;

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

    if (isAppInstalled(com.appcoins.billing.sdk.BuildConfig.CAFE_BAZAAR_PACKAGE_NAME,
        context.getPackageManager()) || userFromIran(getUserCountry(context))) {
      iabAction = com.appcoins.billing.sdk.BuildConfig.CAFE_BAZAAR_IAB_BIND_ACTION;
    } else {
      iabAction = com.appcoins.billing.sdk.BuildConfig.IAB_BIND_ACTION;
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

  private static String chooseServiceToBind(List<String> packageNameServices, String iabAction) {
    if (iabAction.equals(com.appcoins.billing.sdk.BuildConfig.CAFE_BAZAAR_IAB_BIND_ACTION)) {
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

  static void removeNotification() {
    if (hasPopup) {
      notificationManager.cancel(POA_NOTIFICATION_ID);
      hasPopup = false;
    }
  }

  static void createInstallWalletNotification() {
    PackageManager packageManager = context.getPackageManager();
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(CAFE_BAZAAR_APP_URL));
    if (isAppInstalled(BuildConfig.CAFE_BAZAAR_PACKAGE_NAME, packageManager) && isAbleToRedirect(
        intent, packageManager)) {
      intent.setPackage(BuildConfig.CAFE_BAZAAR_PACKAGE_NAME);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      buildNotification(intent);
    } else if (userFromIran(getUserCountry(context))) {
      intent = getNotificationIntentForBrowser(CAFE_BAZAAR_WEB_URL, packageManager);
    } else {
      intent = redirectToRemainingStores(packageManager);
    }
    if (intent != null) {
      createNotification(intent);
    }
  }

  private static Intent redirectToRemainingStores(PackageManager packageManager) {
    Intent intent = getNotificationIntentForStore();
    if (!isAbleToRedirect(intent, packageManager)) {
      intent = getNotificationIntentForBrowser(URL_BROWSER, packageManager);
    }
    return intent;
  }

  private static boolean userFromIran(String userCountry) {
    String loweredUserCountry = userCountry.toLowerCase();
    return loweredUserCountry.equals("ir") || loweredUserCountry.equals("iran");
  }

  private static String getUserCountry(Context context) {
    try {
      TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      String simCountry = tm.getSimCountryIso();
      if (simCountry != null && simCountry.length() == 2) {
        return simCountry;
      } else if (tm.getPhoneType()
          != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
        String networkCountry = tm.getNetworkCountryIso();
        if (networkCountry != null && networkCountry.length() == 2) {
          return networkCountry;
        }
      }
    } catch (Exception ignored) {
    }
    return Locale.getDefault()
        .getCountry();
  }

  private static Intent getNotificationIntentForBrowser(String url, PackageManager packageManager) {
    Intent intent;
    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    Intent notificationIntent = buildNotification(intent);
    if (!isAbleToRedirect(notificationIntent, packageManager)) {
      return null;
    }
    return notificationIntent;
  }

  private static Intent getNotificationIntentForStore() {

    String url = URL_INTENT_INSTALL;
    int verCode = WalletUtils.getAptoideVersion();
    if (verCode != UNINSTALLED_APTOIDE_VERSION_CODE) {
      url += URL_APTOIDE_PARAMETERS + context.getPackageName();
    }

    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    if (verCode >= MINIMUM_APTOIDE_VERSION) {
      intent.setPackage(BuildConfig.APTOIDE_PACKAGE_NAME);
    }

    return buildNotification(intent);
  }

  private static boolean isAbleToRedirect(Intent intent, PackageManager packageManager) {
    ActivityInfo activityInfo = intent.resolveActivityInfo(packageManager, 0);
    return activityInfo != null;
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

    TranslationsModel translationsModel = fetchTranslations();

    builder.setSmallIcon(intent.getExtras()
        .getInt(IDENTIFIER_KEY))
        .setAutoCancel(true)
        .setContentTitle(translationsModel.getPoaNotificationTitle())
        .setContentText(translationsModel.getPoaNotificationBody());
    return builder.build();
  }

  private static TranslationsModel fetchTranslations() {
    Locale locale = Locale.getDefault();
    TranslationsXmlParser translationsParser = new TranslationsXmlParser(context);
    if (iabAction.equals(com.appcoins.billing.sdk.BuildConfig.CAFE_BAZAAR_IAB_BIND_ACTION)) {
      return translationsParser.parseTranslationXml("fa", "IR");
    }
    return translationsParser.parseTranslationXml(locale.getLanguage(), locale.getCountry());
  }

  private static boolean isAppInstalled(String packageName, PackageManager packageManager) {
    try {
      packageManager.getPackageInfo(packageName, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }

  private static void createNotification(Intent intent) {
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

  private static Notification buildNotificationOlderVersion(Intent intent) {

    Notification.Builder builder;
    pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

    builder = new Notification.Builder(context);
    builder.setPriority(Notification.PRIORITY_MAX);
    builder.setContentIntent(pendingIntent);
    builder.setVibrate(new long[0]);

    TranslationsModel translationsModel = fetchTranslations();

    builder.setSmallIcon(intent.getExtras()
        .getInt(IDENTIFIER_KEY))
        .setAutoCancel(true)
        .setContentTitle(translationsModel.getPoaNotificationTitle())
        .setContentText(translationsModel.getPoaNotificationBody());

    return builder.build();
  }
}
