package com.bds.microraidenj.channel;

public class InsufficientFundsException extends Exception {

  InsufficientFundsException(String message) {
    super(message);
  }
}
