package com.appcoins.sdk.billing.payasguest;

import android.content.Intent;
import com.appcoins.sdk.billing.models.WalletGenerationModel;

interface PaymentMethodsView {
  void setSkuInformation(SkuDetailsModel skuDetailsModel);

  void showError();

  void close();

  void showAlertNoBrowserAndStores();

  void redirectToWalletInstallation(Intent intent);

  void navigateToAdyen(String selectedRadioButton);

  void setRadioButtonSelected(String radioButtonSelected);

  void setPositiveButtonText(String text);

  void saveWalletInformation(WalletGenerationModel walletGenerationModel);

  void addPayment(String name);

  void showPaymentView();

  void showBonus(int bonus);
}
