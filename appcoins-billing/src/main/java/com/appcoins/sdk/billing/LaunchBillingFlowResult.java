package com.appcoins.sdk.billing;

public class LaunchBillingFlowResult {
  private final Object responseCode;
  private final Object buyIntent;

  public LaunchBillingFlowResult(Object responseCode, Object buyIntent) {
    this.responseCode = responseCode;
    this.buyIntent = buyIntent;
  }

  public Object getResponseCode() {
    return responseCode;
  }

  public Object getBuyIntent() {
    return buyIntent;
  }
}
