package com.sdk.appcoins_adyen.utils;

public final class StringUtil {

  /**
   * Removes empty spaces and any additional specified characters.
   *
   * @param value The string to be normalized.
   * @param additionalCharsToReplace Additional characters to be removed.
   *
   * @return The original string normalized to remove specified characters.
   */
  public static String normalize(String value, char... additionalCharsToReplace) {
    return value.replaceAll("[\\s" + new String(additionalCharsToReplace) + "]", "");
  }

  /**
   * Check if the string only contains number and the specified separator characters.
   *
   * @param value The string to be checked.
   * @param separators The optional accepted separators.
   *
   * @return If the string is only numbers and separators.
   */
  public static boolean isDigitsAndSeparatorsOnly(String value, char... separators) {
    for (int position = 0; position < value.length(); position++) {
      final char c = value.charAt(position);

      if (!Character.isDigit(c)) {
        return false;
      }

      for (char separator : separators) {
        if (separator != c) {
          return false;
        }
      }
    }

    return true;
  }
}
