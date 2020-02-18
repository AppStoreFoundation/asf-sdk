package com.appcoins.sdk.billing;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesRepository {

  private static final String WALLET_ID_KEY = "WALLET_ID";
  private static final String MAX_BONUS_KEY = "MAX_BONUS";
  private static final String MAX_BONUS_TTL_KEY = "MAX_BONUS_TTL";
  private SharedPreferences sharedPreferences;

  public SharedPreferencesRepository(Context context) {

    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public String getWalletId() {
    return sharedPreferences.getString(WALLET_ID_KEY, null);
  }

  public void setWalletId(String walletId) {
    sharedPreferences.edit()
        .putString(WALLET_ID_KEY, walletId)
        .apply();
  }

  public int getMaxBonus() {
    return sharedPreferences.getInt(MAX_BONUS_KEY, 0);
  }

  public void setMaxBonus(int maxBonus) {
    if (maxBonus > 0) {
      sharedPreferences.edit()
          .putInt(MAX_BONUS_KEY, maxBonus)
          .apply();
      sharedPreferences.edit()
          .putLong(MAX_BONUS_TTL_KEY, System.currentTimeMillis() * 1000)
          .apply();
    }
  }

  public boolean hasSavedBonus(long timeInMillis) {
    if (sharedPreferences.contains(MAX_BONUS_KEY)) {
      if (sharedPreferences.contains(MAX_BONUS_TTL_KEY)) {
        return (timeInMillis * 1000 - sharedPreferences.getLong(MAX_BONUS_TTL_KEY, 0)) < 86400 * 30;
      }
    }
    return false;
  }
}
