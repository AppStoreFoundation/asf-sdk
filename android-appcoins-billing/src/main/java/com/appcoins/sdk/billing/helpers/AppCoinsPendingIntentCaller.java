package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;

public class AppCoinsPendingIntentCaller {

  public static final String BUY_INTENT_RAW = "BUY_INTENT_RAW";
  public static final String BUY_INTENT = "BUY_INTENT";
  public static final AppCoinsPendingIntentCaller instance =
      new AppCoinsPendingIntentCaller(new HashMap<Integer, Intent>());
  private final Map<Integer, Intent> cache;

  AppCoinsPendingIntentCaller(Map<Integer, Intent> cache) {
    this.cache = cache;
  }

  public static void startPendingAppCoinsIntent(Activity activity, IntentSender intentSender,
      int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags)
      throws IntentSender.SendIntentException {
    instance.startPendingIntent(activity, intentSender, requestCode, fillInIntent, flagsMask,
        flagsValues, extraFlags);
  }

  public void saveIntent(Bundle bundle) {
    Parcelable pendingIntent = bundle.getParcelable(BUY_INTENT);
    Parcelable rawIntent = bundle.getParcelable(BUY_INTENT_RAW);
    if (pendingIntent instanceof PendingIntent && rawIntent instanceof Intent) {
      cache.put(((PendingIntent) pendingIntent).getIntentSender()
          .hashCode(), ((Intent) rawIntent));
    }
  }

  private void startPendingIntent(Activity activity, IntentSender intentSender, int requestCode,
      Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags)
      throws IntentSender.SendIntentException {
    Intent intent = cache.get(intentSender.hashCode());
    if (intent == null) {
      activity.startIntentSenderForResult(intentSender, requestCode, fillInIntent, flagsMask,
          flagsValues, extraFlags);
    } else {
      activity.startActivityForResult(intent, requestCode);
    }
  }
}
