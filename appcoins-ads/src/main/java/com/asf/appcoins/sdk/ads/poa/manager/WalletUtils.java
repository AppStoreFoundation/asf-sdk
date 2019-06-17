package com.asf.appcoins.sdk.ads.poa.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.ArrayMap;
import android.util.Log;
import com.asf.appcoins.sdk.ads.BuildConfig;
import com.asf.appcoins.sdk.ads.R;
import java.lang.reflect.Field;

public class WalletUtils {

  private static boolean dialogVisible;

  public static String walletPackageName = BuildConfig.BDS_WALLET_PACKAGE_NAME;

  public static Context context;

  public static boolean isDialogVisible() {
    return dialogVisible;
  }

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

  public static void promptToInstallWallet() {
    final Activity act;
    try {
      act = getActivity();
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    if (act == null) {
      Log.d(WalletUtils.class.getName(),"Problem retrieving main Activity");
      return;
    }

    dialogVisible = true;
    AlertDialog.Builder builder;
    builder = new AlertDialog.Builder(act);
    builder.setTitle(R.string.wallet_missing);
    builder.setMessage(act.getString(R.string.install_wallet_from_iab));
    Log.d("String name: ", act.getString(R.string.install_wallet_from_iab));

    builder.setPositiveButton(R.string.install, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        act.startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + walletPackageName)));
      }
    });

    builder.setNegativeButton(R.string.skip, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
        dialogVisible = false;
      }
    });

    builder.setIcon(android.R.drawable.ic_dialog_alert);
    builder.show();
  }


  public static void createInstallWalletNotification(){


    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + walletPackageName));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


    Notification notification = new Notification.Builder(context)
        .setSmallIcon(R.drawable.ic_stat_name)
        .setContentTitle("APPC Wallet Missing")
        .setContentText("To get your reward in AppCoins, you need to install the AppCoins BDS Wallet.")
        .setAutoCancel(true)
        .addAction(R.drawable.ic_stat_name,"Install",pendingIntent)
        .build();

    NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(0,notification);



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
