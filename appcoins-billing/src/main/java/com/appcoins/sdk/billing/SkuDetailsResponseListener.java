package com.appcoins.sdk.billing;

import java.util.List;

public interface SkuDetailsResponseListener {
  void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList);
}
