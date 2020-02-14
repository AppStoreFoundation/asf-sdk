package com.appcoins.sdk.billing.payasguest;

import android.content.Intent;

interface PaymentMethodsView {
  void setSkuInformation(String fiatPrice, String currencyCode, String appcPrice, String sku);

  void showError();

  void close();

  void showAlertNoBrowserAndStores();

  void redirectToWalletInstallation(Intent intent);

  void navigateToAdyen(String selectedRadioButton);
}
