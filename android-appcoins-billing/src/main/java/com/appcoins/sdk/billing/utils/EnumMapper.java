package com.appcoins.sdk.billing.utils;

public class EnumMapper {

  public Enum parseToEnum(Class enumClass, String string) {
    String snakeCaseString = string.replace(" ", "_")
        .toUpperCase();
    return Enum.valueOf(enumClass, snakeCaseString);
  }
}
