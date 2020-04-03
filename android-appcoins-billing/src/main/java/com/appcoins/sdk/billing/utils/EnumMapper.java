package com.appcoins.sdk.billing.utils;

public class EnumMapper {

  public <T extends Enum<T>> T parseToEnum(Class<T> enumClass, String string, T defaultValue) {
    String snakeCaseString = string.replace(" ", "_")
        .toUpperCase();
    T value = defaultValue;
    try {
      value = Enum.valueOf(enumClass, snakeCaseString);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    return value;
  }
}
