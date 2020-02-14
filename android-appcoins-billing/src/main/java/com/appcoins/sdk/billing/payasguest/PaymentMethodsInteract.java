package com.appcoins.sdk.billing.payasguest;

import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.WalletInteract;
import com.appcoins.sdk.billing.WalletInteractListener;

class PaymentMethodsInteract {

  private WalletInteract walletInteract;

  PaymentMethodsInteract(WalletInteract walletInteract) {

    this.walletInteract = walletInteract;
  }

  String retrieveId() {
    return walletInteract.retrieveId();
  }

  void requestWallet(String id, WalletInteractListener walletInteractListener) {
    walletInteract.requestWallet(id, walletInteractListener);
  }

  void requestSkuDetails(BuyItemProperties buyItemProperties,
      SingleSkuDetailsListener skuDetailsListener) {
    SingleSkuDetailsAsync singleSkuDetailsAsync =
        new SingleSkuDetailsAsync(buyItemProperties, skuDetailsListener);
    singleSkuDetailsAsync.execute();
  }
}
