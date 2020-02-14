package com.appcoins.sdk.billing.payasguest;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.WalletInteractListener;
import com.appcoins.sdk.billing.helpers.WalletInstallationIntentBuilder;
import com.appcoins.sdk.billing.models.WalletGenerationModel;

class PaymentMethodsPresenter {

  private final PaymentMethodsView fragmentView;
  private PaymentMethodsInteract paymentMethodsInteract;
  private WalletInstallationIntentBuilder walletInstallationIntentBuilder;

  public PaymentMethodsPresenter(PaymentMethodsView view,
      PaymentMethodsInteract paymentMethodsInteract,
      WalletInstallationIntentBuilder walletInstallationIntentBuilder) {

    this.fragmentView = view;
    this.paymentMethodsInteract = paymentMethodsInteract;
    this.walletInstallationIntentBuilder = walletInstallationIntentBuilder;
  }

  public void requestWallet() {
    String id = paymentMethodsInteract.retrieveId();
    WalletInteractListener walletInteractListener = new WalletInteractListener() {
      @Override public void walletIdRetrieved(WalletGenerationModel walletGenerationModel) {
        Log.d("TAG123", walletGenerationModel.getWalletAddress()
            + "ewt: "
            + walletGenerationModel.getEwt()
            + " "
            + walletGenerationModel.hasError());
      }
    };
    paymentMethodsInteract.requestWallet(id, walletInteractListener);
  }

  public void provideSkuDetailsInformation(BuyItemProperties buyItemProperties) {
    SingleSkuDetailsListener listener = new SingleSkuDetailsListener() {
      @Override public void onResponse(boolean error, SkuDetails skuDetails) {
        if (error) {
          fragmentView.showError();
        } else {
          fragmentView.setSkuInformation(skuDetails.getFiatPrice(),
              skuDetails.getFiatPriceCurrencyCode(), skuDetails.getAppcPrice(),
              skuDetails.getSku());
        }
      }
    };
    paymentMethodsInteract.requestSkuDetails(buyItemProperties, listener);
  }

  public void onCancelButtonClicked(Button cancelButton) {
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        fragmentView.close();
      }
    });
  }

  public void onPositiveButtonClicked(Button positiveButton, final String selectedRadioButton) {
    positiveButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (selectedRadioButton.equals("paypal") || selectedRadioButton.equals("credit_card")) {
          fragmentView.navigateToAdyen(selectedRadioButton);
        } else {
          Intent intent = walletInstallationIntentBuilder.getWalletInstallationIntent();
          if (intent != null) {
            fragmentView.redirectToWalletInstallation(intent);
          } else {
            fragmentView.showAlertNoBrowserAndStores();
          }
        }
      }
    });
  }
}
