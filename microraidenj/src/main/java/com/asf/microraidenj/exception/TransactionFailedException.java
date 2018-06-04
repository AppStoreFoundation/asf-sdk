package com.asf.microraidenj.exception;

public class TransactionFailedException extends Exception {

  public TransactionFailedException(Throwable cause) {
    super(cause);
  }

  public TransactionFailedException(String message, Exception e) {
    super(message, e);
  }
}
