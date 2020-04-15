package com.sdk.appcoins_adyen.models;

public class ExpiryDate {
  public static final int EMPTY_VALUE = 0;
  public static final ExpiryDate EMPTY_DATE = new ExpiryDate(EMPTY_VALUE, EMPTY_VALUE);

  private final int mExpiryMonth;
  private final int mExpiryYear;

  public ExpiryDate(int expiryMonth, int expiryYear) {
    mExpiryMonth = expiryMonth;
    mExpiryYear = expiryYear;
  }

  public int getExpiryMonth() {
    return mExpiryMonth;
  }

  public int getExpiryYear() {
    return mExpiryYear;
  }
}

