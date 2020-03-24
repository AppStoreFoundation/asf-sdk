package com.appcoins.sdk.billing.helpers;

import android.content.Context;
import android.telephony.TelephonyManager;
import java.util.Locale;

class CafeBazaarUtils {

  static boolean userFromIran(String userCountry) {
    return userCountry.equalsIgnoreCase("ir") || userCountry.equalsIgnoreCase("iran");
  }

  static String getUserCountry(Context context) {
    try {
      TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      String simCountry = tm.getSimCountryIso();
      if (simCountry != null && simCountry.length() == 2) {
        return simCountry;
      } else if (tm.getPhoneType()
          != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
        String networkCountry = tm.getNetworkCountryIso();
        if (networkCountry != null && networkCountry.length() == 2) {
          return networkCountry;
        }
      }
    } catch (Exception ignored) {
    }
    return Locale.getDefault()
        .getCountry();
  }
}
