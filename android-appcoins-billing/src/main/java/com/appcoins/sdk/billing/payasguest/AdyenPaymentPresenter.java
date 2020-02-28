package com.appcoins.sdk.billing.payasguest;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.DeveloperPayload;
import com.appcoins.sdk.billing.listeners.GetTransactionListener;
import com.appcoins.sdk.billing.listeners.LoadPaymentInfoListener;
import com.appcoins.sdk.billing.listeners.MakePaymentListener;
import com.appcoins.sdk.billing.models.AdyenTransactionModel;
import com.appcoins.sdk.billing.models.PaymentMethodsModel;
import com.appcoins.sdk.billing.models.Transaction.Status;
import com.appcoins.sdk.billing.models.TransactionResponse;
import com.appcoins.sdk.billing.service.adyen.AdyenRepository;
import com.sdk.appcoins_adyen.utils.RedirectUtils;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

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

  void loadPaymentInfo() {
    fragmentView.showLoading();
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

  public void onSavedInstance(Bundle savedInstance) {
    waitingResult = savedInstance.getBoolean(WAITING_RESULT_KEY);
  }

  public void onPositiveClick() {

  }

  public void onCancelClick() {
    fragmentView.close();
  }

  public void onErrorButtonClick() {
    fragmentView.close();
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
      fragmentView.showLoading();
      launchPaypal(paymentMethodsModel);
    }
  }

  private void launchPaypal(PaymentMethodsModel paymentMethod) {
    BuyItemProperties buyItemProperties = adyenPaymentInfo.getBuyItemProperties();
    DeveloperPayload developerPayload = buyItemProperties.getDeveloperPayload();
    MakePaymentListener makePaymentListener = new MakePaymentListener() {
      @Override public void onResponse(AdyenTransactionModel adyenTransactionModel) {
        if (!waitingResult) {
          handleModel(adyenTransactionModel);
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

  private void handleModel(final AdyenTransactionModel adyenTransactionModel) {
    if (adyenTransactionModel.hasError()) {
      fragmentView.showError();
    } else {
      ActivityResultListener activityResultListener = new ActivityResultListener() {
        @Override public void onActivityResult(Uri data) {
          String uid = adyenTransactionModel.getUid();
          JSONObject details = RedirectUtils.parseRedirectResult(data);
          String paymentData = null;
          MakePaymentListener makePaymentListener = new MakePaymentListener() {
            @Override public void onResponse(AdyenTransactionModel adyenTransactionModel) {
              if (adyenTransactionModel.hasError()) {
                fragmentView.showError();
              } else {
                handlePaymentResult(adyenTransactionModel.getUid(),
                    adyenTransactionModel.getResultCode(),
                    adyenTransactionModel.getRefusalReasonCode(),
                    adyenTransactionModel.getRefusalReason(), adyenTransactionModel.getStatus());
              }
            }
          };
          adyenPaymentInteract.submitRedirect(uid, adyenPaymentInfo.getWalletAddress(), details,
              paymentData, makePaymentListener);
        }
      };
      fragmentView.lockRotation();
      fragmentView.navigateToUri(adyenTransactionModel.getUrl(), activityResultListener);
      waitingResult = true;
      //Analytics
    }
  }

  private void handlePaymentResult(String uid, String resultCode, String refusalReasonCode,
      String refusalReason, String status) {
    if (resultCode.equalsIgnoreCase("AUTHORISED")) {
      handleSuccessAdyenTransaction(uid);
    } else if (status.equalsIgnoreCase(Status.CANCELED.toString())) {
      fragmentView.close();
    } else if (refusalReason != null && refusalReasonCode != null) {
      //Improve later with specific errors
      fragmentView.showError();
    } else {
      fragmentView.showError();
    }
  }

  private void handleSuccessAdyenTransaction(final String uid) {
    //requestWallet
    //requestWalletListener{
    final GetTransactionListener getTransactionListener = new GetTransactionListener() {
      @Override public void onResponse(TransactionResponse transactionResponse) {
        if (transactionResponse.hasError()) {
          fragmentView.showError();
        } else {
          if (transactionResponse.getStatus()
              .equalsIgnoreCase(String.valueOf(Status.COMPLETED))) {
            Log.d("TAG123", "COMPLETED");
          } else if (paymentFailed(transactionResponse.getStatus())) {
            Log.d("TAG123", "FAILED");
          } else {
            Log.d("TAG123", "REPEAT");
            new Timer().schedule(new TimerTask() {
              @Override public void run() {
                handleSuccessAdyenTransaction(uid);
              }
            }, 3000);
          }
        }
      }
    };
    adyenPaymentInteract.getTransaction(uid, adyenPaymentInfo.getWalletAddress(),
        adyenPaymentInfo.getSignature(), getTransactionListener);
  }

  private boolean paymentFailed(String status) {
    return status.equalsIgnoreCase(String.valueOf(Status.FAILED)) || status.equalsIgnoreCase(
        String.valueOf(Status.CANCELED)) || status.equalsIgnoreCase(
        String.valueOf(Status.INVALID_TRANSACTION));
  }

  private AdyenRepository.Methods mapPaymentToService(String paymentType) {
    if (paymentType.equals(PaymentMethodsFragment.CREDIT_CARD_RADIO)) {
      return AdyenRepository.Methods.CREDIT_CARD;
    } else {
      return AdyenRepository.Methods.PAYPAL;
    }
  }
}
