package com.asf.appcoins.sdk.core.util.wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.asf.appcoins.sdk.core.R;
import com.asf.appcoins.sdk.core.util.AndroidUtils;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class WalletUtils {

  public static boolean hasWalletInstalled(Context context) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    Uri uri = Uri.parse("ethereum:");
    intent.setData(uri);

    return AndroidUtils.hasHandlerAvailable(intent, context);
  }

  public static Single<Boolean> promptToInstallWallet(Activity activity, String message) {
    return showWalletInstallDialog(activity, message).doOnSuccess(aBoolean -> {
      if (aBoolean) {
        gotoStore(activity);
      }
    });
  }

  private static Single<Boolean> showWalletInstallDialog(Context context, String message) {
    return Single.create(emitter -> {
      AlertDialog.Builder builder;
      builder = new AlertDialog.Builder(context);
      builder.setTitle(R.string.wallet_missing)
          .setMessage(message)
          .setPositiveButton(R.string.install, (dialog, which) -> emitter.onSuccess(true))
          .setNegativeButton(R.string.skip, (dialog, which) -> emitter.onSuccess(false))
          .setIcon(android.R.drawable.ic_dialog_alert)
          .show();
    });
  }

  @NonNull private static void gotoStore(Context activity) {
    String appPackageName = "com.asfoundation.wallet";
    try {
      activity.startActivity(
          new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
    } catch (android.content.ActivityNotFoundException anfe) {
      activity.startActivity(new Intent(Intent.ACTION_VIEW,
          Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
    }
  }
}
