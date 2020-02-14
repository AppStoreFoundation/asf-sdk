package com.appcoins.sdk.billing.payasguest;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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

  void requestWallet() {
    String id = paymentMethodsInteract.retrieveId();
    WalletInteractListener walletInteractListener = new WalletInteractListener() {
      @Override public void walletIdRetrieved(WalletGenerationModel walletGenerationModel) {
        if (walletGenerationModel.hasError()) {
          fragmentView.showError();
        } else {
          fragmentView.saveWalletInformation(walletGenerationModel);
        }
      }
    };
    paymentMethodsInteract.requestWallet(id, walletInteractListener);
  }

  void provideSkuDetailsInformation(BuyItemProperties buyItemProperties) {
    SingleSkuDetailsListener listener = new SingleSkuDetailsListener() {
      @Override public void onResponse(boolean error, SkuDetails skuDetails) {
        if (error) {
          fragmentView.showError();
        } else {
          fragmentView.setSkuInformation(
              new SkuDetailsModel(skuDetails.getFiatPrice(), skuDetails.getFiatPriceCurrencyCode(),
                  skuDetails.getAppcPrice(), skuDetails.getSku()));
        }
      }
    };
    paymentMethodsInteract.requestSkuDetails(buyItemProperties, listener);
  }

  void onCancelButtonClicked(Button cancelButton) {
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        fragmentView.close();
      }
    });
  }

  void onPositiveButtonClicked(Button positiveButton, final String selectedRadioButton) {
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

  void onRadioButtonClicked(RadioButton creditCardButton, RadioButton paypalButton,
      RadioButton installRadioButton, RelativeLayout creditWrapper, RelativeLayout paypalWrapper,
      RelativeLayout installWrapper) {
    RadioButtonClickListener creditCardListener =
        new RadioButtonClickListener(PaymentMethodsFragment.CREDIT_CARD_RADIO);
    RadioButtonClickListener paypalListener =
        new RadioButtonClickListener(PaymentMethodsFragment.PAYPAL_RADIO);
    RadioButtonClickListener installListener =
        new RadioButtonClickListener(PaymentMethodsFragment.INSTALL_RADIO);

    creditCardButton.setOnClickListener(creditCardListener);
    creditWrapper.setOnClickListener(creditCardListener);
    paypalButton.setOnClickListener(paypalListener);
    paypalWrapper.setOnClickListener(paypalListener);
    installRadioButton.setOnClickListener(installListener);
    installWrapper.setOnClickListener(installListener);
  }

  void onErrorButtonClicked(Button errorButton) {
    errorButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        fragmentView.close();
      }
    });
  }

  public class RadioButtonClickListener implements View.OnClickListener {

    private String selectedRadioButton;

    RadioButtonClickListener(String selectedRadioButton) {
      this.selectedRadioButton = selectedRadioButton;
    }

    @Override public void onClick(View view) {
      fragmentView.setRadioButtonSelected(selectedRadioButton);
      fragmentView.setPositiveButtonText(selectedRadioButton);
    }
  }
}
