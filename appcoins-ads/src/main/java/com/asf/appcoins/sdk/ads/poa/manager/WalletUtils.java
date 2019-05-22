package com.asf.appcoins.sdk.ads.poa.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.appcoins.sdk.billing.wallet.DialogWalletInstall;
import com.asf.appcoins.sdk.ads.BuildConfig;
import com.asf.appcoins.sdk.ads.R;

public class WalletUtils {

    private static String walletPackageName = BuildConfig.BDS_WALLET_PACKAGE_NAME;

    public static boolean hasWalletInstalled(Context context) {
        PackageManager packageManager = context.getPackageManager();

        try {
            packageManager.getPackageInfo(walletPackageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void promptToInstallWallet(Activity activity, Context context, String message,
                                             DialogVisibleListener dialogVisibleListener) {
        showWalletInstallDialog(activity, context, message, dialogVisibleListener);
    }

    private static void showWalletInstallDialog(final Activity activity, Context context, String message, final DialogVisibleListener dialogVisibleListener) {
        DialogWalletInstall
                .with(activity, false)
                .show();
    }
}
