package com.asf.appcoins.sdk.iab.exception;

public final class PaymentFailedException extends Exception {

  public PaymentFailedException(String message) {
    super(message);
  }

  public PaymentFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
