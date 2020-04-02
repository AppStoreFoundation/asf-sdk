package com.appcoins.sdk.billing.payasguest;

import android.content.Intent;
import com.appcoins.sdk.billing.models.billing.SkuDetailsModel;
import com.appcoins.sdk.billing.models.billing.SkuPurchase;
import com.appcoins.sdk.billing.models.payasguest.WalletGenerationModel;

interface PaymentMethodsView {
  void setSkuInformation(SkuDetailsModel skuDetailsModel);

  void showError();

  void close(boolean withError);

  void closeWithBillingUnavailable();

  void showAlertNoBrowserAndStores();

  void redirectToWalletInstallation(Intent intent);

  void navigateToAdyen(String paymentMethod);

  void resumeAdyenTransaction(String paymentMethod, String uid);

  void setRadioButtonSelected(String radioButtonSelected);

  void setPositiveButtonText(String text);

  void saveWalletInformation(WalletGenerationModel walletGenerationModel);

  void addPayment(String name);

  void showPaymentView();

  void showBonus(int bonus);

  void showInstallDialog();

  void showItemAlreadyOwnedError(SkuPurchase skuPurchase);

  void hideDialog();

  void hideInstallOption();

  void redirectToSupportEmail(String packageName, String sku, String sdkVersionName,
      int mobileVersion);

  void sendPurchaseStartEvent(String appcPrice);
}
