package com.appcoins.sdk.billing;

public interface Repository {

  PurchasesResult getPurchases(String skuType) throws ServiceConnectionException;
}
