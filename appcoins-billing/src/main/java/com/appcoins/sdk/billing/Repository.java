package com.appcoins.sdk.billing;

import java.util.List;

public interface Repository {

  PurchasesResult getPurchases(String skuType) throws ServiceConnectionException;

  SkuDetailsResult querySkuDetailsAsync(String skuType, List<String> sku)
      throws ServiceConnectionException;

  int consumeAsync(String purchaseToken) throws ServiceConnectionException;

  LaunchBillingFlowResult launchBillingFlow(String skuType,String sku, String payload)
      throws ServiceConnectionException;

  boolean isReady();
}
