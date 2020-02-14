package com.appcoins.sdk.billing.payasguest;

import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.WalletInteract;
import com.appcoins.sdk.billing.WalletInteractListener;

class PaymentMethodsInteract {

  private WalletInteract walletInteract;

  public PaymentMethodsInteract(WalletInteract walletInteract) {

    this.walletInteract = walletInteract;
  }

  public String retrieveId() {
    return walletInteract.retrieveId();
  }

  public void requestWallet(String id, WalletInteractListener walletInteractListener) {
    walletInteract.requestWallet(id, walletInteractListener);
  }

  public void requestSkuDetails(BuyItemProperties buyItemProperties,
      SingleSkuDetailsListener skuDetailsListener) {
    SingleSkuDetailsAsync singleSkuDetailsAsync =
        new SingleSkuDetailsAsync(buyItemProperties, skuDetailsListener);
    singleSkuDetailsAsync.execute();
  }
}
