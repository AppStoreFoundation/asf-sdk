package com.appcoins.sdk.billing.payasguest;

import com.appcoins.sdk.billing.service.BdsService;
import com.appcoins.sdk.billing.service.RequestResponse;
import com.appcoins.sdk.billing.service.ServiceResponseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class PaymentMethodsRepository {

  private BdsService bdsService;

  PaymentMethodsRepository(BdsService bdsService) {
    this.bdsService = bdsService;
  }

  public void loadPaymentMethods(String fiatPrice, String fiatCurrency,
      final PaymentMethodsListener paymentMethodsListener) {
    Map<String, String> queries = new LinkedHashMap<>();
    queries.put("price.value", fiatPrice);
    queries.put("price.currency", fiatCurrency);
    queries.put("currency.type", "fiat");
    ServiceResponseListener serviceResponseListener = new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        PaymentMethodsResponseMapper paymentMethodsResponseMapper =
            new PaymentMethodsResponseMapper();
        paymentMethodsListener.onResponse(paymentMethodsResponseMapper.map(requestResponse));
      }
    };
    bdsService.makeRequest("/broker/8.20191014/methods", "GET", new ArrayList<String>(), queries,
        new HashMap<String, Object>(), true, serviceResponseListener);
  }
}
