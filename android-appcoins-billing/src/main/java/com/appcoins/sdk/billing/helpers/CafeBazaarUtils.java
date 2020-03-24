package com.appcoins.sdk.billing.helpers;

import android.content.Context;
import android.telephony.TelephonyManager;
import java.util.Locale;

class CafeBazaarUtils {

  static boolean userFromIran(String userCountry) {
    return userCountry.equalsIgnoreCase("ir") || userCountry.equalsIgnoreCase("iran");
  }

  static String getUserCountry(Context context) {
    TelephonyManager telephonyManager =
        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    String userCountry = Locale.getDefault()
        .getCountry();
    String simCountry = telephonyManager.getSimCountryIso();
    if (hasCorrectCountryFormat(simCountry)) {
      userCountry = simCountry;
    } else if (isPhoneTypeReliable(telephonyManager)) { // device is not 3G (would be unreliable)
      String networkCountry = telephonyManager.getNetworkCountryIso();
      if (hasCorrectCountryFormat(networkCountry)) {
        userCountry = networkCountry;
      }
    }
    return userCountry;
  }

  private static boolean hasCorrectCountryFormat(String country) {
    return country != null && country.length() == 2;
  }

  private static boolean isPhoneTypeReliable(TelephonyManager telephonyManager) {
    return telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA;
  }
}
