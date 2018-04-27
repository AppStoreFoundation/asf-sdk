package com.asf.appcoins.sdk.core.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.List;

/**
 * Created by neuro on 15-03-2018.
 */

public class AndroidUtils {

  private AndroidUtils(Context context) {
  }

  public static boolean hasHandlerAvailable(Intent intent, Context context) {
    PackageManager manager = context.getPackageManager();
    List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
    return !infos.isEmpty();
  }
}
