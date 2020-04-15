package com.appcoins.sdk.billing.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.appcoins.billing.sdk.BuildConfig;

import static com.appcoins.sdk.billing.helpers.CafeBazaarUtils.getUserCountry;
import static com.appcoins.sdk.billing.helpers.CafeBazaarUtils.userFromIran;

public class WalletInstallationIntentBuilder {

  private final static int MINIMUM_APTOIDE_VERSION = 9908;
  private final String GOOGLE_PLAY_URL =
      "https://play.google.com/store/apps/details?id=" + BuildConfig.BDS_WALLET_PACKAGE_NAME;
  private final String CAFE_BAZAAR_APP_URL = "bazaar://details?id=com.hezardastan.wallet";
  private final String CAFE_BAZAAR_WEB_URL = "https://cafebazaar.ir/app/com.hezardastaan.wallet";
  private final String storeUrl;
  private Context context;
  private PackageManager packageManager;

  public WalletInstallationIntentBuilder(PackageManager packageManager, String packageName,
      Context context) {
    this.packageManager = packageManager;
    this.context = context;
    storeUrl = "market://details?id="
        + BuildConfig.BDS_WALLET_PACKAGE_NAME
        + "&utm_source=appcoinssdk&app_source="
        + packageName;
  }

  public Intent getWalletInstallationIntent() {
    final Intent cafeBazaarIntent = buildBrowserIntent(CAFE_BAZAAR_APP_URL);
    if (WalletUtils.isAppInstalled(BuildConfig.CAFE_BAZAAR_PACKAGE_NAME, packageManager)
        && isAbleToRedirect(cafeBazaarIntent)) {
      cafeBazaarIntent.setPackage(BuildConfig.CAFE_BAZAAR_PACKAGE_NAME);
      return cafeBazaarIntent;
    } else if (userFromIran(getUserCountry(context))) {
      return startActivityForBrowser(CAFE_BAZAAR_WEB_URL);
    } else {
      return redirectToRemainingStores(storeUrl);
    }
  }

  private Intent redirectToRemainingStores(String storeUrl) {
    Intent storeIntent = buildStoreViewIntent(storeUrl);
    if (isAbleToRedirect(storeIntent)) {
      return storeIntent;
    } else {
      return startActivityForBrowser(GOOGLE_PLAY_URL);
    }
  }

  private Intent buildStoreViewIntent(String storeUrl) {
    final Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl));
    if (WalletUtils.getAptoideVersion() >= MINIMUM_APTOIDE_VERSION) {
      appStoreIntent.setPackage(BuildConfig.APTOIDE_PACKAGE_NAME);
    }
    return appStoreIntent;
  }

  private Intent startActivityForBrowser(String url) {
    Intent browserIntent = buildBrowserIntent(url);
    if (isAbleToRedirect(browserIntent)) {
      return browserIntent;
    } else {
      return null;
    }
  }

  private Intent buildBrowserIntent(String url) {
    return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
  }

  private boolean isAbleToRedirect(Intent intent) {
    ActivityInfo activityInfo = intent.resolveActivityInfo(packageManager, 0);
    return activityInfo != null;
  }
}
