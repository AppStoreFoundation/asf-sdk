package com.sdk.appcoins_adyen.exceptions;

public class EncryptionException extends CheckoutException {

  private static final long serialVersionUID = 604047691381396990L;

  public EncryptionException(String message, Throwable cause) {
    super(message, cause);
  }
}