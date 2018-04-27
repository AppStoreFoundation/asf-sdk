package com.asf.appcoins.sdk.core.util.wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.asf.appcoins.sdk.core.util.AndroidUtils;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WalletUtils {

  public static boolean hasWalletInstalled(Context context) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    Uri uri = Uri.parse("ethereum:");
    intent.setData(uri);

    return AndroidUtils.hasHandlerAvailable(intent, context);
  }

  public static void promptToInstallWallet(Activity activity) {
    Disposable subscribe = showWalletInstallDialog(activity).filter(aBoolean -> aBoolean)
        .doOnSuccess(gotoStore(activity))
        .subscribe(aBoolean -> {
        }, Throwable::printStackTrace);
  }

  private static Single<Boolean> showWalletInstallDialog(Context context) {
    return Single.create(emitter -> {
      AlertDialog.Builder builder;
      builder = new AlertDialog.Builder(context);
      builder.setTitle("APPC Wallet Missing")
          .setMessage("To complete your purchase, you have to install an AppCoins wallet")
          .setPositiveButton(android.R.string.yes, (dialog, which) -> emitter.onSuccess(true))
          .setNegativeButton(android.R.string.no, (dialog, which) -> emitter.onSuccess(false))
          .setIcon(android.R.drawable.ic_dialog_alert)
          .show();
    });
  }

  @NonNull private static Consumer<Boolean> gotoStore(Context activity) {
    return aBoolean -> {
      String appPackageName = "com.asfoundation.wallet";
      try {
        activity.startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
      } catch (android.content.ActivityNotFoundException anfe) {
        activity.startActivity(new Intent(Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
      }
    };
  }
}
