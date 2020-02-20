package com.sdk.appcoins_adyen.utils;

import android.text.TextUtils;
import com.sdk.appcoins_adyen.card.CardType;
import com.sdk.appcoins_adyen.card.ValidatedField;
import com.sdk.appcoins_adyen.models.ExpiryDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public final class CardValidationUtils {

  public static final int MAXIMUM_CARD_NUMBER_LENGTH = 19;
  public static final int MAX_DIGIT_SEPARATOR_COUNT = 4;
  public static final int GENERAL_CARD_NUMBER_LENGTH = 16;
  public static final int AMEX_CARD_NUMBER_LENGTH = 15;
  public static final int CVV_MAX_LENGTH = 4;
  public static final int DATE_MAX_LENGTH = 5;
  private static final String PUBLIC_KEY_PATTERN = "([0-9]){5}\\|([A-Z]|[0-9]){512}";
  private static final char DIGIT_SEPARATOR = ' ';
  // Luhn Check
  private static final int RADIX = 10;
  private static final int FIVE_DIGIT = 5;
  // Card Number
  private static final int MINIMUM_CARD_NUMBER_LENGTH = 8;
  // Security Code
  private static final int GENERAL_CARD_SECURITY_CODE_SIZE = 3;
  private static final int AMEX_SECURITY_CODE_SIZE = 4;
  // Date
  private static final int MONTHS_IN_YEAR = 12;
  private static final int MAXIMUM_YEARS_IN_FUTURE = 20;
  private static final int MAXIMUM_EXPIRED_MONTHS = 3;

  public static boolean isPublicKeyValid(String publicKey) {
    final Pattern pubKeyPattern = Pattern.compile(PUBLIC_KEY_PATTERN);
    return !TextUtils.isEmpty(publicKey) && pubKeyPattern.matcher(publicKey)
        .find();
  }

  /**
   * Validate card number.
   */
  public static boolean isValidCardNumber(String number) {

    final String normalizedNumber = StringUtil.normalize(number);
    final int length = normalizedNumber.length();

    final ValidatedField.Validation validation;

    if (!StringUtil.isDigitsAndSeparatorsOnly(normalizedNumber)) {
      return false;
    } else if (length > MAXIMUM_CARD_NUMBER_LENGTH) {
      return false;
    } else if (length < MINIMUM_CARD_NUMBER_LENGTH) {
      return false;
    } else if (isLuhnChecksumValid(normalizedNumber)) {
      return true;
    } else if (length == MAXIMUM_CARD_NUMBER_LENGTH) {
      return false;
    } else {
      return false;
    }
  }

  @SuppressWarnings("checkstyle:MagicNumber")
  private static boolean isLuhnChecksumValid(String normalizedNumber) {
    int s1 = 0;
    int s2 = 0;
    final String reverse = new StringBuffer(normalizedNumber).reverse()
        .toString();

    for (int i = 0; i < reverse.length(); i++) {

      final int digit = Character.digit(reverse.charAt(i), RADIX);

      if (i % 2 == 0) {
        s1 += digit;
      } else {
        s2 += 2 * digit;

        if (digit >= FIVE_DIGIT) {
          s2 -= 9;
        }
      }
    }

    return (s1 + s2) % 10 == 0;
  }

  /**
   * Validate Expiry Date.
   */
  public static ValidatedField<ExpiryDate> validateExpiryDate(ExpiryDate expiryDate) {

    if (dateExists(expiryDate)) {
      final Calendar expiryDateCalendar = getExpiryCalendar(expiryDate);

      final Calendar maxFutureCalendar = GregorianCalendar.getInstance();
      maxFutureCalendar.add(Calendar.YEAR, MAXIMUM_YEARS_IN_FUTURE);

      final Calendar maxPastCalendar = GregorianCalendar.getInstance();
      maxPastCalendar.add(Calendar.MONTH, -MAXIMUM_EXPIRED_MONTHS);

      // higher than maxPast and lower than maxFuture
      if (expiryDateCalendar.compareTo(maxPastCalendar) >= 0
          && expiryDateCalendar.compareTo(maxFutureCalendar) <= 0) {
        return new ValidatedField<>(expiryDate, ValidatedField.Validation.VALID);
      }
    }

    return new ValidatedField<>(expiryDate, ValidatedField.Validation.INVALID);
  }

  /**
   * Validate Security Code.
   * We always pass CardType null, but we can enforce size validation for Amex or otherwise if
   * necessary.
   */
  @SuppressWarnings("SameParameterValue") public static ValidatedField<String> validateSecurityCode(
      String securityCode, CardType cardType) {
    final String normalizedSecurityCode = StringUtil.normalize(securityCode);
    final int length = normalizedSecurityCode.length();

    ValidatedField.Validation validation = ValidatedField.Validation.INVALID;

    if (StringUtil.isDigitsAndSeparatorsOnly(normalizedSecurityCode)) {
      if (cardType == CardType.AMERICAN_EXPRESS && length == AMEX_SECURITY_CODE_SIZE) {
        validation = ValidatedField.Validation.VALID;
      } else if (length == GENERAL_CARD_SECURITY_CODE_SIZE
          && cardType != CardType.AMERICAN_EXPRESS) {
        validation = ValidatedField.Validation.VALID;
      }
    }

    return new ValidatedField<>(normalizedSecurityCode, validation);
  }

  private static boolean dateExists(ExpiryDate expiryDate) {
    return expiryDate != ExpiryDate.EMPTY_DATE
        && expiryDate.getExpiryMonth() != ExpiryDate.EMPTY_VALUE
        && expiryDate.getExpiryYear() != ExpiryDate.EMPTY_VALUE
        && isValidMonth(expiryDate.getExpiryMonth())
        && expiryDate.getExpiryYear() > 0;
  }

  private static boolean isValidMonth(int month) {
    return month > 0 && month <= MONTHS_IN_YEAR;
  }

  private static Calendar getExpiryCalendar(ExpiryDate expiryDate) {

    final Calendar expiryCalendar = GregorianCalendar.getInstance();
    expiryCalendar.clear();
    // First day of the expiry month. Calendar.MONTH is zero-based.
    expiryCalendar.set(expiryDate.getExpiryYear(), expiryDate.getExpiryMonth() - 1, 1);
    // Go to next month and remove 1 day to be on the last day of the expiry month.
    expiryCalendar.add(Calendar.MONTH, 1);
    expiryCalendar.add(Calendar.DAY_OF_MONTH, -1);

    return expiryCalendar;
  }

  private static String getCardNumberRawValue(String text) {
    return text.replace(DIGIT_SEPARATOR + "", "");
  }
}

