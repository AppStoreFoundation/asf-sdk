package com.appcoins.sdk.billing.payasguest;

import com.appcoins.sdk.billing.helpers.ConsumePurchaseListener;
import com.appcoins.sdk.billing.listeners.PurchasesListener;
import com.appcoins.sdk.billing.listeners.PurchasesModel;
import com.appcoins.sdk.billing.listeners.billing.PurchaseListener;
import com.appcoins.sdk.billing.mappers.PurchaseMapper;
import com.appcoins.sdk.billing.models.billing.PurchaseModel;
import com.appcoins.sdk.billing.service.RequestResponse;
import com.appcoins.sdk.billing.service.Service;
import com.appcoins.sdk.billing.service.ServiceResponseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.appcoins.sdk.billing.utils.ServiceUtils.isSuccess;

public class BillingRepository {

  private Service service;

  public BillingRepository(Service service) {

    this.service = service;
  }

  void getSkuPurchase(String packageName, String sku, String walletAddress, String walletSignature,
      final PurchaseListener purchaseListener) {
    ServiceResponseListener serviceResponseListener = new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        PurchaseMapper purchaseMapper = new PurchaseMapper();
        PurchaseModel purchaseModel = purchaseMapper.map(requestResponse);
        purchaseListener.onResponse(purchaseModel);
      }
    };

    List<String> path = new ArrayList<>();
    path.add(packageName);
    path.add("products");
    path.add(sku);
    path.add("purchase");

    Map<String, String> queries = new HashMap<>();
    queries.put("wallet.address", walletAddress);
    queries.put("wallet.signature", walletSignature);
    service.makeRequest("/inapp/8.20180518/packages", "GET", path, queries, null, null,
        serviceResponseListener);
  }

  public void getPurchases(String packageName, String walletAddress, String signedWallet,
      String type, final PurchasesListener purchasesListener) {
    ServiceResponseListener serviceResponseListener = new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        PurchaseMapper purchaseMapper = new PurchaseMapper();
        PurchasesModel purchasesModel = purchaseMapper.mapList(requestResponse);
        purchasesListener.onResponse(purchasesModel);
      }
    };

    List<String> path = new ArrayList<>();
    path.add(packageName);
    path.add("purchases");

    Map<String, String> queries = new HashMap<>();
    queries.put("wallet.address", walletAddress);
    queries.put("wallet.signature", signedWallet);
    queries.put("type", type);

    service.makeRequest("/inapp/8.20180518/packages", "GET", path, queries,
        new HashMap<String, String>(), new HashMap<String, Object>(), serviceResponseListener);
  }

  public void cancelRequests() {
    service.cancelRequests();
  }

  public void consumePurchase(String walletAddress, String signature, String packageName,
      String purchaseToken, final ConsumePurchaseListener consumePurchaseListener) {
    ServiceResponseListener serviceResponseListener = new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        consumePurchaseListener.onConsumed(isSuccess(requestResponse.getResponseCode()));
      }
    };
    List<String> path = new ArrayList<>();
    path.add(packageName);
    path.add("purchases");
    path.add(purchaseToken);

    Map<String, String> queries = new HashMap<>();
    queries.put("wallet.address", walletAddress);
    queries.put("wallet.signature", signature);

    Map<String, Object> body = new HashMap<>();
    body.put("status", "CONSUMED");

    service.makeRequest("inapp/8.20180518/packages", "PATCH", path, queries,
        new HashMap<String, String>(), body, serviceResponseListener);
  }
}
