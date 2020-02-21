package com.appcoins.sdk.billing.payasguest;

import android.os.AsyncTask;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.WalletInteract;
import com.appcoins.sdk.billing.WalletInteractListener;

class PaymentMethodsInteract {

  private final PaymentMethodsRepository paymentMethodsRepository;
  private WalletInteract walletInteract;
  private GamificationInteract gamificationInteract;

  PaymentMethodsInteract(WalletInteract walletInteract, GamificationInteract gamificationInteract,
      PaymentMethodsRepository paymentMethodsRepository) {

    this.walletInteract = walletInteract;
    this.gamificationInteract = gamificationInteract;
    this.paymentMethodsRepository = paymentMethodsRepository;
  }

  String retrieveWalletId() {
    return walletInteract.retrieveWalletId();
  }

  void requestWallet(String id, WalletInteractListener walletInteractListener) {
    walletInteract.requestWallet(id, walletInteractListener);
  }

  void requestSkuDetails(BuyItemProperties buyItemProperties,
      SingleSkuDetailsListener skuDetailsListener) {
    SingleSkuDetailsAsync singleSkuDetailsAsync =
        new SingleSkuDetailsAsync(buyItemProperties, skuDetailsListener);
    singleSkuDetailsAsync.execute(AsyncTask.THREAD_POOL_EXECUTOR);
  }

  void loadPaymentsAvailable(String fiatPrice, String fiatCurrency,
      PaymentMethodsListener paymentMethodsListener) {
    paymentMethodsRepository.loadPaymentMethods(fiatPrice, fiatCurrency, paymentMethodsListener);
  }

  void requestMaxBonus(MaxBonusListener maxBonusListener) {
    gamificationInteract.loadMaxBonus(maxBonusListener);
  }
}
