package com.asf.appcoins.sdk.ads.repository;

public enum ResponseCode {
  ERROR(6), OK(0);

  private int value;

  ResponseCode(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}