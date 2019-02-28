package com.appcoins.sdk.billing;

import java.util.List;

public class SkuDetailsResult {

  private final List<SkuDetails> skuDetailsList;

  private final int responseCode;

  public SkuDetailsResult(List<SkuDetails> skuDetailsList, int responseCode) {
    this.skuDetailsList = skuDetailsList;
    this.responseCode = responseCode;
  }

  public List<SkuDetails> getSkuDetailsList() {
    return skuDetailsList;
  }

  public int getResponseCode() {
    return responseCode;
  }
}
