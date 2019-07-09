package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import com.appcoins.sdk.android.billing.BuildConfig;
import com.appcoins.sdk.android.billing.R;
import java.lang.ref.WeakReference;

import static android.provider.CalendarContract.CalendarCache.URI;

public class WalletUtils {

  private static final String TAG = AppcoinsBillingStubHelper.class.getSimpleName();

  public static WeakReference<Activity> context;

  public static void setContext(Activity cont) {
    context = new WeakReference<>(cont);
  }

  public static boolean hasWalletInstalled() {
    PackageManager packageManager = context.get()
        .getPackageManager();

    try {
      packageManager.getPackageInfo(BuildConfig.BDS_WALLET_PACKAGE_NAME, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
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
    try {
      act = getActivity();
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    if (act == null) {
      return;
    }

    String url = "market://details?id="
        + BuildConfig.BDS_WALLET_PACKAGE_NAME
        + "&utm_source=appcoinssdk&app_source="
        + context.get()
        .getPackageName();

    final Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

    AlertDialog.Builder builder;
    builder = new AlertDialog.Builder(act);
    builder.setTitle(R.string.wallet_missing);
    builder.setMessage(R.string.install_wallet_from_iab);
    Log.d("String name: ", act.getString(R.string.install_wallet_from_iab));

    builder.setPositiveButton(R.string.install, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        OnInstallClickAction(act, appStoreIntent);
      }
    });

    builder.setNegativeButton(R.string.skip, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
      }
    });

    builder.setIcon(android.R.drawable.ic_dialog_alert);
    builder.show();
  }

  private static void OnInstallClickAction(Activity act, Intent appStoreIntent) {
    if (hasAptoideInstalled()) {
      Log.d(TAG, "Aptoide is installed on this device");
      appStoreIntent.setPackage(BuildConfig.APTOIDE_PACKAGE_NAME);
    }
    try {
      act.startActivity(appStoreIntent);
    } catch (android.content.ActivityNotFoundException exception) {
      act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
          "https://play.google.com/store/apps/details?id=" + BuildConfig.BDS_WALLET_PACKAGE_NAME)));
    }
  }

  public static Activity getActivity() {
    return context.get();
  }
}
