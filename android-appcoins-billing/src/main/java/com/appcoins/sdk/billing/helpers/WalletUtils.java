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
import com.appcoins.sdk.billing.wallet.DialogWalletInstall;

import java.lang.reflect.Field;

public class WalletUtils {

    public static String walletPackageName = BuildConfig.BDS_WALLET_PACKAGE_NAME;

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

        /** Here is important to know in advance if the host app has feature graphic,
         *  1- this boolean hasImage is needed to change layout dynamically
         *  2- if so, we need to get  url of this image and then when copy this code to  apk-migrator as Smali,
         *  the correct dialog_wallet_install_graphic needs to be write  */
        DialogWalletInstall
                .with(act)
                .show();
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
