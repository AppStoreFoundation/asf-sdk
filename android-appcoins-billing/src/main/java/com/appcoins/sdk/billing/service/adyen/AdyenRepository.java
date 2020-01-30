package com.appcoins.sdk.billing.service.adyen;

import com.appcoins.sdk.billing.listeners.GetTransactionListener;
import com.appcoins.sdk.billing.listeners.LoadPaymentInfoListener;
import com.appcoins.sdk.billing.listeners.MakePaymentListener;
import com.appcoins.sdk.billing.listeners.NoInfoResponseListener;
import com.appcoins.sdk.billing.service.Service;
import com.appcoins.sdk.billing.service.ServiceResponseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdyenRepository {

  private Service bdsService;
  private AdyenListenerProvider adyenListenerProvider;

  public AdyenRepository(Service bdsService, AdyenListenerProvider adyenListenerProvider) {

    this.bdsService = bdsService;
    this.adyenListenerProvider = adyenListenerProvider;
  }

  public void loadPaymentInfo(String method, String value, String currency, String walletAddress,
      final LoadPaymentInfoListener listener) {
    Map<String, String> queries = new LinkedHashMap<>();
    queries.put("wallet.address", walletAddress);
    queries.put("price.value", value);
    queries.put("price.currency", currency);
    queries.put("method", method);
    ServiceResponseListener serviceResponseListener =
        adyenListenerProvider.createLoadPaymentInfoListener(listener);

    bdsService.makeRequest("payment-methods", "GET", new ArrayList<String>(), queries, null,
        serviceResponseListener);
  }

  public void makePayment(String adyenPaymentMethod, boolean shouldStorePaymentMethod,
      String returnUrl, String value, String currency, String reference, String paymentType,
      String walletAddress, String origin, String packageName, String metadata, String sku,
      String callbackUrl, String transactionType, String developerWallet, String storeWallet,
      String oemWallet, String userWallet, final MakePaymentListener listener) {
    Map<String, String> queries = new HashMap<>();
    queries.put("wallet.address", walletAddress);

    Map<String, String> body =
        buildMakePaymentBody(adyenPaymentMethod, shouldStorePaymentMethod, returnUrl, value,
            currency, reference, paymentType, origin, packageName, metadata, sku, callbackUrl,
            transactionType, developerWallet, storeWallet, oemWallet, userWallet);
    ServiceResponseListener serviceResponseListener =
        adyenListenerProvider.createMakePaymentListener(listener);

    bdsService.makeRequest("transactions", "POST", new ArrayList<String>(), queries, body,
        serviceResponseListener);
  }

  public void getTransaction(String uid, String walletAddress, String walletSignature,
      GetTransactionListener getTransactionListener) {

    ServiceResponseListener serviceResponseListener =
        adyenListenerProvider.createGetTransactionListener(getTransactionListener);

    List<String> path = new ArrayList<>();
    path.add(uid);

    Map<String, String> queries = new HashMap<>();
    queries.put("wallet.address", walletAddress);
    queries.put("wallet.signature", walletSignature);

    bdsService.makeRequest("transactions", "GET", path, queries, null, serviceResponseListener);
  }

  public void submitRedirect(String uid, String walletAddress, Object details, String data,
      final MakePaymentListener makePaymentListener) {
    ServiceResponseListener serviceResponseListener =
        adyenListenerProvider.createSubmitRedirectListener(makePaymentListener);

    List<String> path = new ArrayList<>();
    path.add(uid);

    Map<String, String> queries = new LinkedHashMap<>();
    queries.put("wallet.address", walletAddress);

    Map<String, String> body = new LinkedHashMap<>();
    putIfNotNull(body, "payment.details", details.toString());
    putIfNotNull(body, "payment.data", data);

    bdsService.makeRequest("transactions", "PATCH", path, queries, body, serviceResponseListener);
  }

  public void disablePayments(String walletAddress,
      final NoInfoResponseListener noInfoResponseListener) {
    ServiceResponseListener serviceResponseListener =
        adyenListenerProvider.createDisablePaymentsListener(noInfoResponseListener);

    Map<String, String> body = new LinkedHashMap<>();
    body.put("wallet.address", walletAddress);

    bdsService.makeRequest("disable-recurring", "POST", null, null, body, serviceResponseListener);
  }

  private Map<String, String> buildMakePaymentBody(String adyenPaymentMethod,
      boolean shouldStorePaymentMethod, String returnUrl, String value, String currency,
      String reference, String paymentType, String origin, String packageName, String metadata,
      String sku, String callbackUrl, String transactionType, String developerWallet,
      String storeWallet, String oemWallet, String userWallet) {
    Map<String, String> body = new LinkedHashMap<>();
    body.put("payment.method", adyenPaymentMethod);
    body.put("price.currency", enclosedString(currency));
    body.put("wallets.developer", enclosedString(developerWallet));
    body.put("domain", enclosedString(packageName));
    body.put("metadata", enclosedString(metadata));
    body.put("method", enclosedString(paymentType));
    body.put("wallets.oem", enclosedString(oemWallet));
    body.put("origin", enclosedString(origin));
    body.put("reference", enclosedString(reference));
    body.put("payment.return_url", enclosedString(returnUrl));
    body.put("payment.store_method", Boolean.toString(shouldStorePaymentMethod));
    body.put("product", enclosedString(sku));
    body.put("wallets.store", enclosedString(storeWallet));
    body.put("type", enclosedString(transactionType));
    body.put("wallets.user", enclosedString(userWallet));
    body.put("price.value", enclosedString(value));
    putIfNotNull(body, "callback_url", enclosedString(callbackUrl));
    return body;
  }

  private void putIfNotNull(Map<String, String> map, String key, String value) {
    if (value != null && !value.equals("\"null\"")) {
      map.put(key, value);
    }
  }

  private String enclosedString(String value) {
    return "\"" + value + "\"";
  }
}
