package com.appcoins.sdk.billing.service;

public class RequestResponse {

  private final int responseCode;
  private final String response;
  private Exception exception;

  /**
   * @param responseCode Response code returned by the request, 500 by default (if there's a non IO
   * exception))
   * @param response Response returned by the request
   * @param exception Exception returned by the request
   */
  public RequestResponse(int responseCode, String response, Exception exception) {

    this.responseCode = responseCode;
    this.response = response;
    this.exception = exception;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public String getResponse() {
    return response;
  }

  public Exception getException() {
    return exception;
  }
}
