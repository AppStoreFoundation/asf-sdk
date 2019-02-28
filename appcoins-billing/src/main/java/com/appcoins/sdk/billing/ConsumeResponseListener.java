package com.appcoins.sdk.billing;

public interface ConsumeResponseListener {

  void onConsumeResponse(int responseCode, String purchaseToken);
}
