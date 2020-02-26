package com.appcoins.sdk.billing.payasguest;

import android.os.Bundle;
import android.util.Log;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.DeveloperPayload;
import com.appcoins.sdk.billing.listeners.LoadPaymentInfoListener;
import com.appcoins.sdk.billing.listeners.MakePaymentListener;
import com.appcoins.sdk.billing.models.AdyenTransactionModel;
import com.appcoins.sdk.billing.models.PaymentMethodsModel;
import com.appcoins.sdk.billing.service.adyen.AdyenRepository;

class AdyenPaymentPresenter {

  private final static String WAITING_RESULT_KEY = "waiting_result";
  private final AdyenPaymentView fragmentView;
  private final AdyenPaymentInfo adyenPaymentInfo;
  private final AdyenPaymentInteract adyenPaymentInteract;
  private String returnUrl;
  private boolean waitingResult;

  public AdyenPaymentPresenter(AdyenPaymentView fragmentView, AdyenPaymentInfo adyenPaymentInfo,
      AdyenPaymentInteract adyenPaymentInteract, String returnUrl) {
    this.fragmentView = fragmentView;
    this.adyenPaymentInfo = adyenPaymentInfo;
    this.adyenPaymentInteract = adyenPaymentInteract;
    this.returnUrl = returnUrl;
    waitingResult = false;
  }

  public void loadPaymentInfo() {
    AdyenRepository.Methods method = mapPaymentToService(adyenPaymentInfo.getPaymentMethod());
    LoadPaymentInfoListener loadPaymentInfoListener = new LoadPaymentInfoListener() {
      @Override public void onResponse(PaymentMethodsModel paymentMethodsModel) {
        if (paymentMethodsModel.hasError()) {
          fragmentView.showError();
        } else {
          fragmentView.updateFiatPrice(paymentMethodsModel.getValue(),
              paymentMethodsModel.getCurrency());
          launchPayment(paymentMethodsModel);
        }
      }
    };
    adyenPaymentInteract.loadPaymentInfo(method, adyenPaymentInfo.getFiatPrice(),
        adyenPaymentInfo.getFiatCurrency(), adyenPaymentInfo.getWalletAddress(),
        loadPaymentInfoListener);
  }

  public void onSaveInstanceState(Bundle outState) {
    outState.putBoolean(WAITING_RESULT_KEY, waitingResult);
  }

  public void onSavedInstace(Bundle savedInstance) {
    waitingResult = savedInstance.getBoolean(WAITING_RESULT_KEY);
  }

  public void onPositiveClick() {

  }

  public void onCancelClick() {
    fragmentView.close();
  }

  public void onErrorButtonClick() {

  }

  public void onChangeCardClick() {

  }

  public void onMorePaymentsClick() {

  }

  private void launchPayment(PaymentMethodsModel paymentMethodsModel) {
    if (adyenPaymentInfo.getPaymentMethod()
        .equals(PaymentMethodsFragment.CREDIT_CARD_RADIO)) {
      fragmentView.showCreditCardView();
    } else {
      launchPaypal(paymentMethodsModel);
    }
  }

  private void launchPaypal(PaymentMethodsModel paymentMethod) {
    BuyItemProperties buyItemProperties = adyenPaymentInfo.getBuyItemProperties();
    DeveloperPayload developerPayload = buyItemProperties.getDeveloperPayload();
    MakePaymentListener makePaymentListener = new MakePaymentListener() {
      @Override public void onResponse(AdyenTransactionModel adyenTransactionModel) {
        if (adyenTransactionModel.hasError()) {
          Log.d("TAG123", "ERROR");
        } else {
          Log.d("TAG123", "SUCCESS");
        }
      }
    };
    adyenPaymentInteract.makePayment(paymentMethod.getPaymentMethod(), false, returnUrl,
        paymentMethod.getValue()
            .toString(), paymentMethod.getCurrency(), developerPayload.getOrderReference(),
        mapPaymentToService(adyenPaymentInfo.getPaymentMethod()).getTransactionType(),
        developerPayload.getOrigin(), buyItemProperties.getPackageName(),
        developerPayload.getDeveloperPayload(), buyItemProperties.getSku(), null, "INAPP",
        adyenPaymentInfo.getWalletAddress(), makePaymentListener);
  }

  private AdyenRepository.Methods mapPaymentToService(String paymentType) {
    if (paymentType.equals(PaymentMethodsFragment.CREDIT_CARD_RADIO)) {
      return AdyenRepository.Methods.CREDIT_CARD;
    } else {
      return AdyenRepository.Methods.PAYPAL;
    }
  }
}
