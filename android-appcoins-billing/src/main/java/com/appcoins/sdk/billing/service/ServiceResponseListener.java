package com.appcoins.sdk.billing.service;

public interface ServiceResponseListener {

  /**
   * @param code Response code returned by the request, 500 by default (if there's a non IO
   * exception))
   * @param apiResponse Response returned by the request
   * @param exception Exception returned by the request
   */
  void onResponseReceived(int code, String apiResponse, Exception exception);
}
