package com.appcoins.sdk.billing.utils;

public class ServiceUtils {

  public static boolean isSuccess(int code) {
    return code >= 200 && code < 300;
  }
}
