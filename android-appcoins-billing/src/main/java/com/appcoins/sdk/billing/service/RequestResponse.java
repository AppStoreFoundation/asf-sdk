package com.appcoins.sdk.billing.service;

public class RequestResponse {

  private final int responseCode;
  private final String response;
  private Exception exception;

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
