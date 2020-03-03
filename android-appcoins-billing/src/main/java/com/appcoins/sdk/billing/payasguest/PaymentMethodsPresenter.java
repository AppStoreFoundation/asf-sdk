package com.appcoins.sdk.billing.payasguest;

import android.content.Intent;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.WalletInteractListener;
import com.appcoins.sdk.billing.helpers.WalletInstallationIntentBuilder;
import com.appcoins.sdk.billing.models.WalletGenerationModel;

class PaymentMethodsPresenter {

  private final PaymentMethodsView fragmentView;
  private PaymentMethodsInteract paymentMethodsInteract;
  private WalletInstallationIntentBuilder walletInstallationIntentBuilder;

  PaymentMethodsPresenter(PaymentMethodsView view, PaymentMethodsInteract paymentMethodsInteract,
      WalletInstallationIntentBuilder walletInstallationIntentBuilder) {

    this.fragmentView = view;
    this.paymentMethodsInteract = paymentMethodsInteract;
    this.walletInstallationIntentBuilder = walletInstallationIntentBuilder;
  }

  void prepareUi(final BuyItemProperties buyItemProperties) {
    String id = paymentMethodsInteract.retrieveWalletId();
    WalletInteractListener walletInteractListener = new WalletInteractListener() {
      @Override public void walletIdRetrieved(WalletGenerationModel walletGenerationModel) {
        fragmentView.saveWalletInformation(walletGenerationModel);
        provideSkuDetailsInformation(buyItemProperties, walletGenerationModel.hasError());
      }
    };
    paymentMethodsInteract.requestWallet(id, walletInteractListener);
    MaxBonusListener maxBonusListener = new MaxBonusListener() {
      @Override public void onBonusReceived(int bonus) {
        fragmentView.showBonus(bonus);
      }
    };
    paymentMethodsInteract.requestMaxBonus(maxBonusListener);
  }

  void onCancelButtonClicked() {
    fragmentView.close();
  }

  void onPositiveButtonClicked(String selectedRadioButton) {
    if (selectedRadioButton.equals("paypal") || selectedRadioButton.equals("credit_card")) {
      fragmentView.navigateToAdyen(selectedRadioButton);
    } else {
      Intent intent = walletInstallationIntentBuilder.getWalletInstallationIntent();
      if (intent != null) {
        if (intent.getPackage() != null && intent.getPackage()
            .equals(BuildConfig.APTOIDE_PACKAGE_NAME)) {
          fragmentView.hideDialog();
        }
        fragmentView.redirectToWalletInstallation(intent);
      } else {
        fragmentView.showAlertNoBrowserAndStores();
      }
    }
  }

  void onRadioButtonClicked(String selectedRadioButton) {
    fragmentView.setRadioButtonSelected(selectedRadioButton);
    fragmentView.setPositiveButtonText(selectedRadioButton);
  }

  void onErrorButtonClicked() {
    fragmentView.close();
  }

  private void provideSkuDetailsInformation(BuyItemProperties buyItemProperties,
      boolean walletGenerated) {
    if (!walletGenerated) {
      SingleSkuDetailsListener listener = new SingleSkuDetailsListener() {
        @Override public void onResponse(boolean error, SkuDetails skuDetails) {
          if (!error) {
            loadPaymentsAvailable(skuDetails.getFiatPrice(), skuDetails.getFiatPriceCurrencyCode());
            fragmentView.setSkuInformation(new SkuDetailsModel(skuDetails.getFiatPrice(),
                skuDetails.getFiatPriceCurrencyCode(), skuDetails.getAppcPrice(),
                skuDetails.getSku()));
          } else {
            fragmentView.showPaymentView();
          }
        }
      };
      paymentMethodsInteract.requestSkuDetails(buyItemProperties, listener);
    } else {
      fragmentView.showPaymentView();
    }
  }

  private void loadPaymentsAvailable(String fiatPrice, String fiatCurrency) {
    PaymentMethodsListener paymentMethodsListener = new PaymentMethodsListener() {
      @Override public void onResponse(PaymentMethodsModel paymentMethodsModel) {
        if (!paymentMethodsModel.hasError()) {
          for (PaymentMethod paymentMethod : paymentMethodsModel.getPaymentMethods()) {
            if (paymentMethod.isAvailable()) {
              fragmentView.addPayment(paymentMethod.getName());
            }
          }
        }
        fragmentView.showPaymentView();
      }
    };
    paymentMethodsInteract.loadPaymentsAvailable(fiatPrice, fiatCurrency, paymentMethodsListener);
  }
}
