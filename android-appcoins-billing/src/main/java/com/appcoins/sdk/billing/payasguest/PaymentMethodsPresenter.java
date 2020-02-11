package com.appcoins.sdk.billing.payasguest;

import android.util.Log;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.WalletInteract;
import com.appcoins.sdk.billing.WalletInteractListener;
import com.appcoins.sdk.billing.models.WalletGenerationModel;

class PaymentMethodsPresenter {

  private PaymentMethodsView view;
  private WalletInteract walletInteract;

  public PaymentMethodsPresenter(PaymentMethodsView view, WalletInteract walletInteract) {

    this.view = view;
    this.walletInteract = walletInteract;
  }

  public void requestWallet() {
    String id = walletInteract.retrieveId();
    WalletInteractListener walletInteractListener = new WalletInteractListener() {
      @Override public void walletIdRetrieved(WalletGenerationModel walletGenerationModel) {
        Log.d("TAG123", walletGenerationModel.getWalletAddress()
            + "ewt: "
            + walletGenerationModel.getEwt()
            + " "
            + walletGenerationModel.hasError());
      }
    };
    walletInteract.requestWallet(id, walletInteractListener);
  }

  public void provideSkuDetailsInformation(BuyItemProperties buyItemProperties) {
    SingleSkuDetailsListener listener = new SingleSkuDetailsListener() {
      @Override public void onResponse(boolean error, SkuDetails skuDetails) {
        if (error) {
          view.showError();
        } else {
          view.setSkuInformation(skuDetails.getFiatPrice(), skuDetails.getFiatPriceCurrencyCode(),
              skuDetails.getAppcPrice(), skuDetails.getSku());
        }
      }
    };
    SingleSkuDetailsAsync singleSkuDetailsAsync =
        new SingleSkuDetailsAsync(buyItemProperties, listener);
    singleSkuDetailsAsync.execute();
  }
}
