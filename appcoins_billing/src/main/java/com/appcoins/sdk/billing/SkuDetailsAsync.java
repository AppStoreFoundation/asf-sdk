package com.appcoins.sdk.billing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SkuDetailsAsync implements Runnable {

  private final Repository repository;
  private SkuDetailsResponseListener skuDetailsResponseListener;
  private SkuDetailsParams skuDetailsParams;
  private static final String DETAILS_LIST = "DETAILS_LIST";

  public SkuDetailsAsync(SkuDetailsParams skuDetailsParams,
      SkuDetailsResponseListener skuDetailsResponseListener, Repository repository) {
    this.skuDetailsParams = skuDetailsParams;
    this.skuDetailsResponseListener = skuDetailsResponseListener;
    this.repository = repository;
  }

  @Override public void run() {
    try {
      SkuDetailsResult response = getSkuDetails();

      if (response.getSkuDetailsList() == null || response.getSkuDetailsList().size() == 0) {
        skuDetailsResponseListener.onSkuDetailsResponse(-1, new ArrayList<SkuDetails>());
      } else {
        skuDetailsResponseListener.onSkuDetailsResponse(0, response.getSkuDetailsList());
      }

    } catch (ServiceConnectionException e) {
      e.printStackTrace();
      skuDetailsResponseListener.onSkuDetailsResponse(-1, new ArrayList<SkuDetails>());
    }
  }

  private SkuDetailsResult getSkuDetails() throws ServiceConnectionException {
    return repository.querySkuDetailsAsync(skuDetailsParams.getItemType(),
        skuDetailsParams.getMoreItemSkus());
  }
}
