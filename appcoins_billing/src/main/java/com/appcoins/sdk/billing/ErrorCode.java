package com.appcoins.sdk.billing;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {
  BILLING_UNAVAILABLE(3), DEVELOPER_ERROR(5), ERROR(6), FEATURE_NOT_SUPPORTED(
      -2), ITEM_ALREADY_OWNED(7), ITEM_NOT_OWNED(8), ITEM_UNAVAILABLE(4), OK(
      0), SERVICE_DISCONNECTED(-1), SERVICE_UNAVAILABLE(2), USER_CANCELED(1);

  private int value;
  private static Map map = new HashMap<>();

  ErrorCode(int value) {
    this.value = value;
  }

  static {
    for (ErrorCode pageType : ErrorCode.values()) {
      map.put(pageType.value, pageType);
    }
  }

  public static ErrorCode valueOf(int pageType) {
    return (ErrorCode) map.get(pageType);
  }

  public int getValue() {
    return value;
  }
}