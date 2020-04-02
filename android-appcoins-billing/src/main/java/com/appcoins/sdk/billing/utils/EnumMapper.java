package com.appcoins.sdk.billing.utils;

public class EnumMapper {

  public Enum parseToEnum(Class enumClass, String string, Enum defaultValue) {
    String snakeCaseString = string.replace(" ", "_")
        .toUpperCase();
    Enum value = defaultValue;
    try {
      value = Enum.valueOf(enumClass, snakeCaseString);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    return value;
  }
}
