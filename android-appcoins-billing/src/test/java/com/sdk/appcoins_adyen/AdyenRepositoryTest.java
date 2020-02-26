package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.listeners.LoadPaymentInfoListener;
import com.appcoins.sdk.billing.listeners.MakePaymentListener;
import com.appcoins.sdk.billing.models.AdyenPaymentParams;
import com.appcoins.sdk.billing.models.AdyenTransactionModel;
import com.appcoins.sdk.billing.models.PaymentMethodsModel;
import com.appcoins.sdk.billing.models.TransactionInformation;
import com.appcoins.sdk.billing.models.TransactionWallets;
import com.appcoins.sdk.billing.service.BdsService;
import com.appcoins.sdk.billing.service.RequestResponse;
import com.appcoins.sdk.billing.service.ServiceResponseListener;
import com.appcoins.sdk.billing.service.adyen.AdyenListenerProvider;
import com.appcoins.sdk.billing.service.adyen.AdyenRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdyenRepositoryTest {

  @Mock BdsService bdsService;
  @Mock AdyenListenerProvider adyenListenerProvider;
  private AdyenRepository adyenRepository;

  @Before public void setupTest() {
    MockitoAnnotations.initMocks(this);
    adyenRepository = new AdyenRepository(bdsService, adyenListenerProvider);
  }

  @Test public void loadPaymentInfoTest() {
    Map<String, String> queries = new LinkedHashMap<>();
    queries.put("wallet.address", "0x212");
    queries.put("price.value", "9.06");
    queries.put("price.currency", "EUR");
    queries.put("method", "credit_card");
    LoadPaymentInfoListener loadPaymentInfoListener = new LoadPaymentInfoListener() {
      @Override public void onResponse(PaymentMethodsModel paymentMethodsResponse) {

      }
    };
    ServiceResponseListener serviceResponseListener = new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {

      }
    };
    when(adyenListenerProvider.createLoadPaymentInfoListener(loadPaymentInfoListener)).thenReturn(
        serviceResponseListener);
    adyenRepository.loadPaymentInfo("credit_card", "9.06", "EUR", "0x212", loadPaymentInfoListener);
    verify(bdsService).makeRequest("payment-methods", "GET", new ArrayList<String>(), queries, null,
        serviceResponseListener);
  }

  @Test public void makePaymentTest() {
    Map<String, String> queries = new LinkedHashMap<>();
    queries.put("wallet.address", "0x123");
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("payment.method", "");
    body.put("price.currency", "");
    body.put("domain", "");
    body.put("metadata", "");
    body.put("method", "");
    body.put("origin", "");
    body.put("reference", "");
    body.put("payment.return_url", "");
    body.put("payment.store_method", false);
    body.put("product", "");
    body.put("type", "");
    body.put("price.value", "");
    body.put("wallets.oem", "");
    body.put("wallets.store", "");
    body.put("wallets.user", "");
    body.put("wallets.developer", "");
    body.put("callback_url", "");
    MakePaymentListener makePaymentListener = new MakePaymentListener() {
      @Override public void onResponse(AdyenTransactionModel adyenTransactionResponse) {

      }
    };
    ServiceResponseListener serviceResponseListener = new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {

      }
    };
    when(adyenListenerProvider.createMakePaymentListener(makePaymentListener)).thenReturn(
        serviceResponseListener);
    adyenRepository.makePayment(new AdyenPaymentParams("", false, ""),
        new TransactionInformation("", "", "", "", "", "", "", "", "", ""),
        new TransactionWallets("0x123", "", "", "", ""), makePaymentListener);
    verify(bdsService).makeRequest("transactions", "POST", new ArrayList<String>(), queries, body,
        serviceResponseListener);
  }
}
