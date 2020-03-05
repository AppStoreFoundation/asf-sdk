package com.appcoins.sdk.billing.payasguest;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.DeveloperPayload;
import com.appcoins.sdk.billing.listeners.GetTransactionListener;
import com.appcoins.sdk.billing.listeners.LoadPaymentInfoListener;
import com.appcoins.sdk.billing.listeners.MakePaymentListener;
import com.appcoins.sdk.billing.listeners.NoInfoResponseListener;
import com.appcoins.sdk.billing.models.AdyenTransactionModel;
import com.appcoins.sdk.billing.models.PaymentMethodsModel;
import com.appcoins.sdk.billing.models.Transaction.Status;
import com.appcoins.sdk.billing.models.TransactionResponse;
import com.appcoins.sdk.billing.service.adyen.AdyenRepository;
import com.sdk.appcoins_adyen.encryption.CardEncryptorImpl;
import com.sdk.appcoins_adyen.models.ExpiryDate;
import com.sdk.appcoins_adyen.utils.CardValidationUtils;
import com.sdk.appcoins_adyen.utils.RedirectUtils;
import java.math.BigDecimal;
import org.json.JSONObject;

class AdyenPaymentPresenter {

  private final static String WAITING_RESULT_KEY = "waiting_result";
  private final AdyenPaymentView fragmentView;
  private final AdyenPaymentInfo adyenPaymentInfo;
  private final AdyenPaymentInteract adyenPaymentInteract;
  private String returnUrl;
  private boolean waitingResult;
  private boolean isDestroyed;

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

  public void onPositiveClick(String cardNumber, String expiryDate, String cvv,
      String storedPaymentId, BigDecimal serverFiatPrice, String serverCurrency) {
    fragmentView.showLoading();
    fragmentView.lockRotation();
    CardEncryptorImpl cardEncryptor = new CardEncryptorImpl(BuildConfig.ADYEN_PUBLIC_KEY);
    ExpiryDate mExpiryDate = CardValidationUtils.getDate(expiryDate);
    String encryptedCard;
    if (storedPaymentId.equals("")) {
      encryptedCard = cardEncryptor.encryptFields(cardNumber, mExpiryDate.getExpiryMonth(),
          mExpiryDate.getExpiryYear(), cvv);
    } else {
      encryptedCard = cardEncryptor.encryptStoredPaymentFields(cvv, storedPaymentId, "scheme");
    }

    makePayment(encryptedCard, serverFiatPrice, serverCurrency);
  }

  private void makePayment(String encryptedCard, BigDecimal serverFiatPrice,
      String serverCurrency) {
    BuyItemProperties buyItemProperties = adyenPaymentInfo.getBuyItemProperties();
    DeveloperPayload developerPayload = buyItemProperties.getDeveloperPayload();
    MakePaymentListener makePaymentListener = new MakePaymentListener() {
      @Override public void onResponse(AdyenTransactionModel adyenTransactionModel) {
        if (adyenTransactionModel.hasError()) {
          fragmentView.showError();
        } else {
          handlePaymentResult(adyenTransactionModel.getUid(), adyenTransactionModel.getResultCode(),
              adyenTransactionModel.getRefusalReasonCode(),
              adyenTransactionModel.getRefusalReason(), adyenTransactionModel.getStatus());
        }
      }
    };
    adyenPaymentInteract.makePayment(encryptedCard, true, returnUrl, serverFiatPrice.toString(),
        serverCurrency, developerPayload.getOrderReference(),
        mapPaymentToService(adyenPaymentInfo.getPaymentMethod()).getTransactionType(),
        buyItemProperties.getPackageName(), developerPayload.getDeveloperPayload(),
        buyItemProperties.getSku(), null, buyItemProperties.getType()
            .toUpperCase(), adyenPaymentInfo.getWalletAddress(), makePaymentListener);
  }

  public void onCancelClick() {
    fragmentView.close();
  }

  public void onErrorButtonClick() {
    fragmentView.close();
  }

  public void onChangeCardClick() {
    fragmentView.showLoading();
    NoInfoResponseListener noInfoResponseListener = new NoInfoResponseListener() {
      @Override public void onResponse(boolean error) {
        if (error) {
          fragmentView.showError();
        } else {
          fragmentView.clearCreditCardInput();
          fragmentView.showCreditCardView(null);
        }
      }
    };
    adyenPaymentInteract.forgetCard(adyenPaymentInfo.getWalletAddress(), noInfoResponseListener);
  }

  public void onMorePaymentsClick() {
    fragmentView.navigateToPaymentSelection();
  }

  private void launchPayment(PaymentMethodsModel paymentMethodsModel) {
    if (adyenPaymentInfo.getPaymentMethod()
        .equals(PaymentMethodsFragment.CREDIT_CARD_RADIO)) {
      fragmentView.showCreditCardView(paymentMethodsModel.getStoredMethodDetails());
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
          handlePaypalModel(adyenTransactionModel);
        }
      }
    };
    adyenPaymentInteract.makePayment(paymentMethod.getPaymentMethod(), false, returnUrl,
        paymentMethod.getValue()
            .toString(), paymentMethod.getCurrency(), developerPayload.getOrderReference(),
        mapPaymentToService(adyenPaymentInfo.getPaymentMethod()).getTransactionType(),
        buyItemProperties.getPackageName(), developerPayload.getDeveloperPayload(),
        buyItemProperties.getSku(), null, buyItemProperties.getType()
            .toUpperCase(), adyenPaymentInfo.getWalletAddress(), makePaymentListener);
  }

  private void handlePaypalModel(final AdyenTransactionModel adyenTransactionModel) {
    if (adyenTransactionModel.hasError()) {
      fragmentView.showError();
    } else {
      ActivityResultListener activityResultListener = new ActivityResultListener() {
        @Override public void onActivityResult(Uri data) {
          String uid = adyenTransactionModel.getUid();
          JSONObject details = RedirectUtils.parseRedirectResult(data);
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
              null, makePaymentListener);
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
    GetTransactionListener getTransactionListener = new GetTransactionListener() {
      @Override public void onResponse(TransactionResponse transactionResponse) {
        if (transactionResponse.hasError()) {
          fragmentView.showError();
        } else {
          if (transactionResponse.getStatus()
              .equalsIgnoreCase(String.valueOf(Status.COMPLETED))) {
            createBundle(transactionResponse);
          } else if (paymentFailed(transactionResponse.getStatus())) {
            fragmentView.showError();
          } else {
            requestTransaction(uid, 10000, this);
          }
        }
      }
    };
    adyenPaymentInteract.getTransaction(uid, adyenPaymentInfo.getWalletAddress(),
        adyenPaymentInfo.getSignature(), getTransactionListener);
  }

  private void createBundle(final TransactionResponse transactionResponse) {
    PurchaseListener purchaseListener = new PurchaseListener() {
      @Override public void onResponse(PurchaseModel purchaseModel) {
        BillingMapper billingMapper = new BillingMapper();
        Bundle bundle = billingMapper.map(purchaseModel, transactionResponse.getOrderReference());
        fragmentView.finish(bundle);
      }
    };
    BuyItemProperties buyItemProperties = adyenPaymentInfo.getBuyItemProperties();
    adyenPaymentInteract.getCompletedPurchaseBundle(buyItemProperties.getType(),
        buyItemProperties.getPackageName(), buyItemProperties.getSku(),
        adyenPaymentInfo.getWalletAddress(), adyenPaymentInfo.getSignature(), purchaseListener);
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

  private void requestTransaction(final String uid, long delayInMillis,
      final GetTransactionListener getTransactionListener) {
    final Handler handler = new Handler();
    Runnable runnable = new Runnable() {
      @Override public void run() {
        if (!isDestroyed) {
          adyenPaymentInteract.getTransaction(uid, adyenPaymentInfo.getWalletAddress(),
              adyenPaymentInfo.getSignature(), getTransactionListener);
        }
        handler.removeCallbacks(this);
      }
    };
    handler.postDelayed(runnable, delayInMillis);
  }

  void onDestroy() {
    isDestroyed = true;
    adyenPaymentInteract.cancelRequests();
  }
}
