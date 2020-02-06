package com.appcoins.sdk.billing;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesRepository {

  private static final String WALLET_ID_KEY = "WALLET_ID";
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
}
