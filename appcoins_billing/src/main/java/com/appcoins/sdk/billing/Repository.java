package com.appcoins.sdk.billing;

import java.util.HashMap;
import java.util.List;

public interface Repository {

  PurchasesResult getPurchases(String skuType) throws ServiceConnectionException;

  HashMap<String, Object> querySkuDetailsAsync(String skuType, List<String> sku)
      throws ServiceConnectionException;

  int consumeAsync(String purchaseToken) throws ServiceConnectionException;

  HashMap<String, Object> launchBillingFlow(String skuType,String sku, String payload)
      throws ServiceConnectionException;

}
