package com.appcoins.sdk.billing.service.adyen;

import com.appcoins.sdk.billing.listeners.NoInfoResponseListener;
import com.appcoins.sdk.billing.listeners.billing.GetTransactionListener;
import com.appcoins.sdk.billing.listeners.billing.LoadPaymentInfoListener;
import com.appcoins.sdk.billing.listeners.billing.MakePaymentListener;
import com.appcoins.sdk.billing.service.RequestResponse;
import com.appcoins.sdk.billing.service.ServiceResponseListener;

import static com.appcoins.sdk.billing.utils.ServiceUtils.isSuccess;

public class AdyenListenerProvider {

  private AdyenMapper adyenMapper;

  public AdyenListenerProvider(AdyenMapper adyenMapper) {

    this.adyenMapper = adyenMapper;
  }

  public ServiceResponseListener createLoadPaymentInfoListener(
      final LoadPaymentInfoListener listener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        listener.onResponse(adyenMapper.mapPaymentMethodsResponse(requestResponse));
      }
    };
  }

  public ServiceResponseListener createMakePaymentListener(final MakePaymentListener listener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        listener.onResponse(adyenMapper.mapAdyenTransactionResponse(requestResponse));
      }
    };
  }

  ServiceResponseListener createGetTransactionListener(
      final GetTransactionListener getTransactionListener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
        getTransactionListener.onResponse(adyenMapper.mapTransactionResponse(requestResponse));
      }
    };
  }

  ServiceResponseListener createSubmitRedirectListener(
      final MakePaymentListener makePaymentListener) {
    return new ServiceResponseListener() {
      @Override public void onResponseReceived(RequestResponse requestResponse) {
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
