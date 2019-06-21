package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.ArrayMap;
import android.util.Log;
import com.appcoins.sdk.android.billing.BuildConfig;
import com.appcoins.sdk.android.billing.R;
import java.lang.reflect.Field;

public class WalletUtils {

  public static String walletPackageName = BuildConfig.BDS_WALLET_PACKAGE_NAME;
  public static String aptoidePackageName = BuildConfig.APTOIDE_PACKAGE_NAME;

  public static Context context;

  public static void setContext(Context cont) {
    context = cont;
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

  public static boolean hasAptoideInstalled(String packageName, PackageManager packageManager) {
    try {
      return packageManager.getApplicationInfo(packageName, 0).enabled;
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

    final Intent appStoreIntent =
        new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + walletPackageName));

    AlertDialog.Builder builder;
    builder = new AlertDialog.Builder(act);
    builder.setTitle(R.string.wallet_missing);
    builder.setMessage(act.getString(R.string.install_wallet_from_iab));
    Log.d("String name: ", act.getString(R.string.install_wallet_from_iab));

      builder.setPositiveButton(R.string.install, new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          try {
            if (hasAptoideInstalled(aptoidePackageName, context.getPackageManager())) {
              Log.d("AptoideInstallation", "Aptoide is installed on this device");
              appStoreIntent.setPackage(aptoidePackageName);
            } else {
              Log.d("AptoideInstallation", "Aptoide is not installed on this device");
            }
            act.startActivity(appStoreIntent);
          } catch (android.content.ActivityNotFoundException exception) {
            act.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=" + walletPackageName)));
          }
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

  public static Activity getActivity() throws Exception {
    Class activityThreadClass = Class.forName("android.app.ActivityThread");
    Object activityThread = activityThreadClass.getMethod("currentActivityThread")
        .invoke(null);
    Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
    activitiesField.setAccessible(true);
    ArrayMap activities = (ArrayMap) activitiesField.get(activityThread);
    for (Object activityRecord : activities.values()) {
      Class activityRecordClass = activityRecord.getClass();
      Field pausedField = activityRecordClass.getDeclaredField("paused");
      pausedField.setAccessible(true);
      if (!pausedField.getBoolean(activityRecord)) {
        Field activityField = activityRecordClass.getDeclaredField("activity");
        activityField.setAccessible(true);
        Activity activity = (Activity) activityField.get(activityRecord);
        return activity;
      }
    }
    return null;
  }
}
