package com.appcoins.sdk.billing;

import android.app.PendingIntent;
import android.content.Intent;

public class LaunchBillingFlowResult {
  private final int responseCode;
  private final PendingIntent buyIntent;
  private final Intent rawIntent;

  public LaunchBillingFlowResult(int responseCode, PendingIntent buyIntent, Intent rawIntent) {
    this.responseCode = responseCode;
    this.buyIntent = buyIntent;
    this.rawIntent = rawIntent;
  }

  public Intent getRawIntent() {
    return rawIntent;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public PendingIntent getBuyIntent() {
    return buyIntent;
  }
}
