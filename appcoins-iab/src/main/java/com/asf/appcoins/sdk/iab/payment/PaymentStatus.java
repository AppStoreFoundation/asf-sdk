package com.asf.appcoins.sdk.iab.payment;

import com.asf.appcoins.sdk.core.transaction.Transaction.Status;

/**
 * Created by neuro on 13-03-2018.
 */

public enum PaymentStatus {
  SUCCESS, FAIL, PENDING;

  public static PaymentStatus from(Status status) {
    switch (status) {
      case FAILED:
        return FAIL;
      case PENDING:
        return PENDING;
      case ACCEPTED:
        return SUCCESS;
      default:
        throw new IllegalArgumentException("Status unknown");
    }
  }
}
