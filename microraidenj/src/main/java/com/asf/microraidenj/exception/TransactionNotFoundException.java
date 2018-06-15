package com.asf.microraidenj.exception;

public class TransactionNotFoundException extends Exception {

  public TransactionNotFoundException(String txHash) {
    super("Transaction not found! Hash: " + txHash);
  }
}
