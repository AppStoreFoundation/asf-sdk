package com.appcoins.sdk.billing.service.adyen;

import android.util.Log;
import com.appcoins.sdk.billing.listeners.GetTransactionListener;
import com.appcoins.sdk.billing.listeners.LoadPaymentInfoListener;
import com.appcoins.sdk.billing.listeners.MakePaymentListener;
import com.appcoins.sdk.billing.listeners.NoInfoResponseListener;
import com.appcoins.sdk.billing.service.RequestResponse;
import com.appcoins.sdk.billing.service.ServiceResponseListener;

import static com.appcoins.sdk.billing.utils.ServiceUtils.isSuccess;

public class AdyenListenerProvider {

  private AdyenMapper adyenMapper;

  public AdyenListenerProvider(AdyenMapper adyenMapper) {

    this.adyenMapper = adyenMapper;
  }

  ServiceResponseListener createLoadPaymentInfoListener(final LoadPaymentInfoListener listener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        Log.d("TAG123", "PaymentMethodsResponse: " + requestResponse);
        listener.onResponse(adyenMapper.mapPaymentMethodsResponse(requestResponse));
      }
    };
  }

  ServiceResponseListener createMakePaymentListener(final MakePaymentListener listener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        Log.d("TAG123", "AdyenTransactionResponse: " + requestResponse);
        listener.onResponse(adyenMapper.mapAdyenTransactionResponse(requestResponse));
      }
    };
  }

  ServiceResponseListener createGetTransactionListener(
      final GetTransactionListener getTransactionListener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        Log.d("TAG123", "TransactionResponse: ");
        getTransactionListener.onResponse(adyenMapper.mapTransactionResponse(requestResponse));
      }
    };
  }

  ServiceResponseListener createSubmitRedirectListener(
      final MakePaymentListener makePaymentListener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        Log.d("TAG123", "AdyenTransactionResponse: " + requestResponse);
        makePaymentListener.onResponse(adyenMapper.mapAdyenTransactionResponse(requestResponse));
      }
    };
  }

  ServiceResponseListener createDisablePaymentsListener(
      final NoInfoResponseListener noInfoResponseListener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        noInfoResponseListener.onResponse(!isSuccess(requestResponse.getResponseCode()));
      }
    };
  }
}
