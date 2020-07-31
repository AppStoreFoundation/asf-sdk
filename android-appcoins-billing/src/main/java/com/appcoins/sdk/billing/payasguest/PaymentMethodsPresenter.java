package com.appcoins.sdk.billing.payasguest;

import android.content.Intent;
import android.os.Build;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.WalletInteractListener;
import com.appcoins.sdk.billing.analytics.BillingAnalytics;
import com.appcoins.sdk.billing.helpers.WalletInstallationIntentBuilder;
import com.appcoins.sdk.billing.helpers.WalletUtils;
import com.appcoins.sdk.billing.listeners.PurchasesListener;
import com.appcoins.sdk.billing.listeners.PurchasesModel;
import com.appcoins.sdk.billing.listeners.SingleSkuDetailsListener;
import com.appcoins.sdk.billing.listeners.payasguest.PaymentMethodsListener;
import com.appcoins.sdk.billing.models.GamificationModel;
import com.appcoins.sdk.billing.models.Transaction;
import com.appcoins.sdk.billing.models.TransactionsListModel;
import com.appcoins.sdk.billing.models.billing.RemoteProduct;
import com.appcoins.sdk.billing.models.billing.SkuDetailsModel;
import com.appcoins.sdk.billing.models.billing.SkuPurchase;
import com.appcoins.sdk.billing.models.billing.TransactionModel;
import com.appcoins.sdk.billing.models.payasguest.PaymentMethod;
import com.appcoins.sdk.billing.models.payasguest.PaymentMethodsModel;
import com.appcoins.sdk.billing.models.payasguest.WalletGenerationModel;
import java.util.List;

import static com.appcoins.sdk.billing.payasguest.IabActivity.CREDIT_CARD;
import static com.appcoins.sdk.billing.payasguest.IabActivity.PAYPAL;

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
    WalletInteractListener walletInteractListener = createWalletInteractListener();
    paymentMethodsInteract.requestWallet(id, walletInteractListener);
    MaxBonusListener maxBonusListener = new MaxBonusListener() {
      @Override public void onBonusReceived(GamificationModel gamificationModel) {
        if (isGamificationActive(gamificationModel)) {
          paymentMethodsInteract.saveMaxBonus(gamificationModel.getMaxBonus());
          fragmentView.showBonus(gamificationModel.getMaxBonus());
        }
      }
    };
    paymentMethodsInteract.requestMaxBonus(maxBonusListener);
  }

  void onCancelButtonClicked(String selectedRadioButton) {
    sendPaymentMethodEvent(selectedRadioButton, BillingAnalytics.EVENT_CANCEL);
    fragmentView.close(false);
  }

  void onPositiveButtonClicked(String selectedRadioButton) {
    if (isAdyen(selectedRadioButton)) {
      sendPaymentMethodEvent(selectedRadioButton, BillingAnalytics.EVENT_NEXT);
      fragmentView.navigateToAdyen(selectedRadioButton);
    } else {
      sendPaymentMethodEvent(selectedRadioButton, BillingAnalytics.EVENT_NEXT);
      Intent intent = walletInstallationIntentBuilder.getWalletInstallationIntent();
      handleIntent(intent);
    }
  }

  void onRadioButtonClicked(String selectedRadioButton) {
    fragmentView.setRadioButtonSelected(selectedRadioButton);
    if (selectedRadioButton != null) {
      fragmentView.setPositiveButtonText(selectedRadioButton);
    }
  }

  void onErrorButtonClicked() {
    fragmentView.close(true);
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

  private void provideSkuDetailsInformation(final WalletGenerationModel walletGenerationModel,
      final BuyItemProperties buyItemProperties) {
    SkuDetails skuDetails = buyItemProperties.getSkuDetails();

    paymentMethodsInteract.cacheAppcPrice(skuDetails.getAppcPrice());
    checkForUnfinishedTransaction(buyItemProperties, walletGenerationModel, skuDetails);
    fragmentView.setSkuInformation(
        new SkuDetailsModel(skuDetails.getFiatPrice(), skuDetails.getFiatPriceCurrencyCode(),
            skuDetails.getAppcPrice()));
  }

  private void loadPaymentsAvailable(final String fiatPrice, String fiatCurrency) {
    PaymentMethodsListener paymentMethodsListener = new PaymentMethodsListener() {
      @Override public void onResponse(PaymentMethodsModel paymentMethodsModel) {
        if (paymentMethodsModel.hasError() || paymentMethodsModel.getPaymentMethods()
            .isEmpty()) {
          paymentMethodsInteract.cancelRequests();
          handleShowInstallDialog();
        } else {
          for (PaymentMethod paymentMethod : paymentMethodsModel.getPaymentMethods()) {
            if (paymentMethod.isAvailable()) {
              fragmentView.addPayment(paymentMethod.getName());
            }
          }
          if (!WalletUtils.deviceSupportsWallet(Build.VERSION.SDK_INT)) {
            fragmentView.hideInstallOption();
          }
          fragmentView.sendPurchaseStartEvent(paymentMethodsInteract.getCachedAppcPrice());
          fragmentView.showPaymentView();
        }
      }
    };
    paymentMethodsInteract.loadPaymentsAvailable(fiatPrice, fiatCurrency, paymentMethodsListener);
  }

  private void checkForUnconsumedPurchased(WalletGenerationModel walletGenerationModel,
      final SkuDetails skuDetails, final BuyItemProperties buyItemProperties) {
    PurchasesListener purchasesListener = new PurchasesListener() {
      @Override public void onResponse(PurchasesModel purchasesModel) {
        if (!purchasesModel.hasError()) {
          for (SkuPurchase skuPurchase : purchasesModel.getSkuPurchases()) {
            if (isSelectedItem(skuPurchase.getProduct(), buyItemProperties.getSku())) {
              paymentMethodsInteract.cancelRequests();
              fragmentView.showItemAlreadyOwnedError(skuPurchase);
              return;
            }
          }
        }
        loadPaymentsAvailable(skuDetails.getFiatPrice(), skuDetails.getFiatPriceCurrencyCode());
      }
    };
    paymentMethodsInteract.checkForUnconsumedPurchased(buyItemProperties.getPackageName(),
        walletGenerationModel.getWalletAddress(), walletGenerationModel.getSignature(),
        buyItemProperties.getType(), purchasesListener);
  }

  private boolean isSelectedItem(RemoteProduct product, String sku) {
    return product.getName()
        .equals(sku);
  }

  private void handleShowInstallDialog() {
    if (WalletUtils.deviceSupportsWallet(Build.VERSION.SDK_INT)) {
      fragmentView.showInstallDialog();
    } else {
      fragmentView.closeWithBillingUnavailable();
    }
  }

  private void sendPaymentMethodEvent(String selectedRadioButton, String action) {
    billingAnalytics.sendPaymentMethodEvent(buyItemProperties.getPackageName(),
        buyItemProperties.getSku(), paymentMethodsInteract.getCachedAppcPrice(),
        selectedRadioButton, buyItemProperties.getType(), action);
  }

  private boolean isGamificationActive(GamificationModel gamificationModel) {
    return gamificationModel.getStatus()
        .equalsIgnoreCase("ACTIVE");
  }

  private WalletInteractListener createWalletInteractListener() {
    return new WalletInteractListener() {
      @Override public void walletAddressRetrieved(WalletGenerationModel walletGenerationModel) {
        if (!walletGenerationModel.hasError()) {
          fragmentView.saveWalletInformation(walletGenerationModel);
          provideSkuDetailsInformation(walletGenerationModel, buyItemProperties);
        } else {
          handleShowInstallDialog();
        }
      }
    };
  }

  private void checkForUnfinishedTransaction(final BuyItemProperties buyItemProperties,
      final WalletGenerationModel walletGenerationModel, final SkuDetails skuDetails) {
    TransactionsListener transactionsListener = new TransactionsListener() {
      @Override public void onResponse(TransactionsListModel transactionsListModel) {
        List<TransactionModel> transactionModels = transactionsListModel.getTransactionModelList();
        if (!transactionModels.isEmpty() && shouldResumeTransaction(transactionModels.get(0)
            .getTransaction())) {
          TransactionModel transactionModel = transactionModels.get(0);
          fragmentView.resumeAdyenTransaction(transactionModel.getGateway(),
              transactionModel.getUid());
        } else {
          checkForUnconsumedPurchased(walletGenerationModel, skuDetails, buyItemProperties);
        }
      }
    };
    paymentMethodsInteract.checkForUnfinishedTransaction(walletGenerationModel.getWalletAddress(),
        walletGenerationModel.getSignature(), buyItemProperties.getSku(),
        buyItemProperties.getPackageName(), transactionsListener);
  }

  private boolean isAdyen(String selectedRadioButton) {
    return selectedRadioButton.equals(PAYPAL) || selectedRadioButton.equals(CREDIT_CARD);
  }

  private void handleIntent(Intent intent) {
    if (intent != null) {
      if (isAptoideRedirect(intent)) {
        fragmentView.hideDialog();
      }
      fragmentView.redirectToWalletInstallation(intent);
    } else {
      fragmentView.showAlertNoBrowserAndStores();
    }
  }

  private boolean isAptoideRedirect(Intent intent) {
    return intent.getPackage() != null && intent.getPackage()
        .equals(BuildConfig.APTOIDE_PACKAGE_NAME);
  }

  private boolean shouldResumeTransaction(Transaction transaction) {
    return transaction.getStatus() == Transaction.Status.PROCESSING;
  }
}
