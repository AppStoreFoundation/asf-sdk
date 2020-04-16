package com.appcoins.sdk.billing;

import android.app.PendingIntent;

public class LaunchBillingFlowResult {
  private final int responseCode;
  private final PendingIntent buyIntent;

  public LaunchBillingFlowResult(int responseCode, PendingIntent buyIntent) {
    this.responseCode = responseCode;
    this.buyIntent = buyIntent;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public PendingIntent getBuyIntent() {
    return buyIntent;
  }
}
