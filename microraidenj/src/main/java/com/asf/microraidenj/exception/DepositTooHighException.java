package com.asf.microraidenj.exception;

import java.math.BigInteger;

public class DepositTooHighException extends Exception {

  public DepositTooHighException(BigInteger maxDeposit) {
    super("Maximum deposit is " + maxDeposit.toString(10));
  }
}
