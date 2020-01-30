package com.appcoins.sdk.billing.service.adyen;

import com.appcoins.sdk.billing.listeners.GetTransactionListener;
import com.appcoins.sdk.billing.listeners.LoadPaymentInfoListener;
import com.appcoins.sdk.billing.listeners.MakePaymentListener;
import com.appcoins.sdk.billing.listeners.NoInfoResponseListener;
import com.appcoins.sdk.billing.models.AdyenPaymentParams;
import com.appcoins.sdk.billing.models.TransactionInformation;
import com.appcoins.sdk.billing.models.TransactionWallets;
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

  public void makePayment(AdyenPaymentParams adyenPaymentParams,
      TransactionInformation transactionInformation, TransactionWallets transactionWallets,
      final MakePaymentListener makePaymentListener) {
    Map<String, String> queries = new HashMap<>();
    queries.put("wallet.address", transactionWallets.getMainWalletAddress());

    Map<String, String> body =
        buildMakePaymentBody(adyenPaymentParams, transactionInformation, transactionWallets);
    ServiceResponseListener serviceResponseListener =
        adyenListenerProvider.createMakePaymentListener(makePaymentListener);

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

  private Map<String, String> buildMakePaymentBody(AdyenPaymentParams adyenPaymentParams,
      TransactionInformation transactionInformation, TransactionWallets transactionWallets) {
    Map<String, String> body = new LinkedHashMap<>();
    body.put("payment.method", adyenPaymentParams.getCardPaymentMethod());
    body.put("price.currency", enclosedString(transactionInformation.getCurrency()));
    body.put("wallets.developer", enclosedString(transactionWallets.getDeveloperWalletAddress()));
    body.put("domain", enclosedString(transactionInformation.getPackageName()));
    body.put("metadata", enclosedString(transactionInformation.getMetadata()));
    body.put("method", enclosedString(transactionInformation.getPaymentType()));
    body.put("wallets.oem", enclosedString(transactionWallets.getOemWalletAddress()));
    body.put("origin", enclosedString(transactionInformation.getOrigin()));
    body.put("reference", enclosedString(transactionInformation.getReference()));
    body.put("payment.return_url", enclosedString(adyenPaymentParams.getReturnUrl()));
    body.put("payment.store_method",
        Boolean.toString(adyenPaymentParams.shouldStorePaymentMethod()));
    body.put("product", enclosedString(transactionInformation.getSku()));
    body.put("wallets.store", enclosedString(transactionWallets.getStoreWalletAddress()));
    body.put("type", enclosedString(transactionInformation.getTransactionType()));
    body.put("wallets.user", enclosedString(transactionWallets.getUserWalletAddress()));
    body.put("price.value", enclosedString(transactionInformation.getValue()));
    putIfNotNull(body, "callback_url", enclosedString(transactionInformation.getCallbackUrl()));
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
