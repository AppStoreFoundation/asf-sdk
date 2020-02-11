package com.appcoins.sdk.billing.payasguest;

interface PaymentMethodsView {
  void setSkuInformation(String fiatPrice, String currencyCode, String appcPrice, String sku);

  void showError();
}
