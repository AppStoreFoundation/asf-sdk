package com.appcoins.sdk.billing.payasguest;

import android.content.Intent;
import android.os.Build;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.WalletInteractListener;
import com.appcoins.sdk.billing.analytics.BillingAnalytics;
import com.appcoins.sdk.billing.helpers.WalletInstallationIntentBuilder;
import com.appcoins.sdk.billing.listeners.PurchasesListener;
import com.appcoins.sdk.billing.listeners.PurchasesModel;
import com.appcoins.sdk.billing.listeners.SingleSkuDetailsListener;
import com.appcoins.sdk.billing.listeners.payasguest.PaymentMethodsListener;
import com.appcoins.sdk.billing.models.GamificationModel;
import com.appcoins.sdk.billing.models.billing.SkuDetailsModel;
import com.appcoins.sdk.billing.models.billing.SkuPurchase;
import com.appcoins.sdk.billing.models.payasguest.PaymentMethod;
import com.appcoins.sdk.billing.models.payasguest.PaymentMethodsModel;
import com.appcoins.sdk.billing.models.payasguest.WalletGenerationModel;

class PaymentMethodsPresenter {

  private final PaymentMethodsView fragmentView;
  private PaymentMethodsInteract paymentMethodsInteract;
  private WalletInstallationIntentBuilder walletInstallationIntentBuilder;
  private BillingAnalytics billingAnalytics;
  private BuyItemProperties buyItemProperties;

  PaymentMethodsPresenter(PaymentMethodsView view, PaymentMethodsInteract paymentMethodsInteract,
      WalletInstallationIntentBuilder walletInstallationIntentBuilder,
      BillingAnalytics billingAnalytics, BuyItemProperties buyItemProperties) {

    this.fragmentView = view;
    this.paymentMethodsInteract = paymentMethodsInteract;
    this.walletInstallationIntentBuilder = walletInstallationIntentBuilder;
    this.billingAnalytics = billingAnalytics;
    this.buyItemProperties = buyItemProperties;
  }

  void prepareUi() {
    String id = paymentMethodsInteract.retrieveWalletId();
    WalletInteractListener walletInteractListener = new WalletInteractListener() {
      @Override public void walletIdRetrieved(WalletGenerationModel walletGenerationModel) {
        fragmentView.saveWalletInformation(walletGenerationModel);
        provideSkuDetailsInformation(buyItemProperties, walletGenerationModel.hasError());
        checkForUnconsumedPurchased(buyItemProperties.getPackageName(), buyItemProperties.getSku(),
            walletGenerationModel.getWalletAddress(), walletGenerationModel.getSignature(),
            "INAPP");
      }
    };
    paymentMethodsInteract.requestWallet(id, walletInteractListener);
    MaxBonusListener maxBonusListener = new MaxBonusListener() {
      @Override public void onBonusReceived(GamificationModel gamificationModel) {
        if (gamificationModel.getStatus()
            .equalsIgnoreCase("ACTIVE")) {
          paymentMethodsInteract.saveMaxBonus(gamificationModel.getMaxBonus());
          fragmentView.showBonus(gamificationModel.getMaxBonus());
        }
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
    if (selectedRadioButton != null) {
      fragmentView.setPositiveButtonText(selectedRadioButton);
    }
  }

  void onErrorButtonClicked() {
    fragmentView.close();
  }

  void onHelpTextClicked(BuyItemProperties buyItemProperties) {
    String packageName = buyItemProperties.getPackageName();
    String sku = buyItemProperties.getSku();
    String sdkVersionName = BuildConfig.VERSION_NAME;
    int mobileVersion = Build.VERSION.SDK_INT;
    fragmentView.redirectToSupportEmail(packageName, sku, sdkVersionName, mobileVersion);
  }

  void onDestroy() {
    paymentMethodsInteract.cancelRequests();
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
            fragmentView.showInstallDialog();
          }
        }
      };
      paymentMethodsInteract.requestSkuDetails(buyItemProperties, listener);
    } else {
      fragmentView.showInstallDialog();
    }
  }

  private void loadPaymentsAvailable(final String fiatPrice, String fiatCurrency) {
    PaymentMethodsListener paymentMethodsListener = new PaymentMethodsListener() {
      @Override public void onResponse(PaymentMethodsModel paymentMethodsModel) {
        if (paymentMethodsModel.hasError() || paymentMethodsModel.getPaymentMethods()
            .isEmpty()) {
          paymentMethodsInteract.cancelRequests();
          fragmentView.showInstallDialog();
        } else {
          for (PaymentMethod paymentMethod : paymentMethodsModel.getPaymentMethods()) {
            if (paymentMethod.isAvailable()) {
              fragmentView.addPayment(paymentMethod.getName());
            }
          }
          billingAnalytics.sendPurchaseDetailsEvent(buyItemProperties.getPackageName(),
              buyItemProperties.getSku(), fiatPrice, buyItemProperties.getType());
          fragmentView.showPaymentView();
        }
      }
    };
    paymentMethodsInteract.loadPaymentsAvailable(fiatPrice, fiatCurrency, paymentMethodsListener);
  }

  private void checkForUnconsumedPurchased(String packageName, final String sku,
      String walletAddress, String signature, String type) {
    PurchasesListener purchasesListener = new PurchasesListener() {
      @Override public void onResponse(PurchasesModel purchasesModel) {
        if (!purchasesModel.hasError()) {
          for (SkuPurchase skuPurchase : purchasesModel.getSkuPurchases()) {
            if (skuPurchase.getProduct()
                .getName()
                .equals(sku)) {
              paymentMethodsInteract.cancelRequests();
              fragmentView.showItemAlreadyOwnedError(skuPurchase);
              return;
            }
          }
        }
      }
    };
    paymentMethodsInteract.checkForUnconsumedPurchased(packageName, walletAddress, signature,
        type.toLowerCase(), purchasesListener);
  }
}
