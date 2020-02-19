package com.sdk.appcoins_adyen.utils;

import android.content.Context;

public class RedirectUtils {

  public static final String REDIRECT_RESULT_SCHEME = "adyencheckout://";

  public static String getReturnUrl(Context context) {
    return REDIRECT_RESULT_SCHEME + context.getPackageName();
  }
}
