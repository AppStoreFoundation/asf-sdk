package com.appcoins.sdk.billing;

public class LaunchBillingFlowResult {
  private final Object responseCode;
  private final Object buyIntent;
  private boolean hasWalletInstalled;

  public LaunchBillingFlowResult(Object responseCode, Object buyIntent,
      boolean hasWalletInstalled) {
    this.responseCode = responseCode;
    this.buyIntent = buyIntent;
    this.hasWalletInstalled = hasWalletInstalled;
  }

  public Object getResponseCode() {
    return responseCode;
  }

  public Object getBuyIntent() {
    return buyIntent;
  }

  public boolean isHasWalletInstalled() {
    return hasWalletInstalled;
  }
}
