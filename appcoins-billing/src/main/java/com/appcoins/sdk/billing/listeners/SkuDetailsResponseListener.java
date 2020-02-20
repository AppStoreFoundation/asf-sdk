package com.appcoins.sdk.billing.listeners;

import com.appcoins.sdk.billing.SkuDetails;
import java.util.List;

public interface SkuDetailsResponseListener {
  void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList);
}
