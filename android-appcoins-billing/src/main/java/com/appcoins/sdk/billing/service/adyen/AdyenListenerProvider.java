package com.appcoins.sdk.billing.service.adyen;

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
        listener.onResponse(adyenMapper.mapPaymentMethodsResponse(requestResponse.getResponseCode(),
            requestResponse.getResponse()));
      }
    };
  }

  ServiceResponseListener createMakePaymentListener(final MakePaymentListener listener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        listener.onResponse(
            adyenMapper.mapAdyenTransactionResponse(requestResponse.getResponseCode(),
                requestResponse.getResponse()));
      }
    };
  }

  ServiceResponseListener createGetTransactionListener(
      final GetTransactionListener getTransactionListener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        getTransactionListener.onResponse(
            adyenMapper.mapTransactionResponse(requestResponse.getResponseCode(),
                requestResponse.getResponse()));
      }
    };
  }

  ServiceResponseListener createSubmitRedirectListener(
      final MakePaymentListener makePaymentListener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        makePaymentListener.onResponse(
            adyenMapper.mapAdyenTransactionResponse(requestResponse.getResponseCode(),
                requestResponse.getResponse()));
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
