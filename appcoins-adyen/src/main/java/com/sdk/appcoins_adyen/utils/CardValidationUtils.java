package com.sdk.appcoins_adyen.utils;

import android.text.TextUtils;
import com.sdk.appcoins_adyen.card.ValidatedField;
import com.sdk.appcoins_adyen.models.ExpiryDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

public final class CardValidationUtils {

  public static final int MAXIMUM_CARD_NUMBER_LENGTH = 19;
  public static final int MAX_DIGIT_SEPARATOR_COUNT = 4;
  public static final int GENERAL_CARD_NUMBER_LENGTH = 16;
  public static final int AMEX_CARD_NUMBER_LENGTH = 15;
  public static final int CVV_MAX_LENGTH = 4;
  public static final int DATE_MAX_LENGTH = 5;
  private static final String DATE_FORMAT = "MM/yy";
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
   * Validate Security Code.
   */
  public static boolean isValidSecurityCode(String securityCode) {
    final String normalizedSecurityCode = StringUtil.normalize(securityCode);
    final int length = normalizedSecurityCode.length();

    return StringUtil.isDigitsAndSeparatorsOnly(normalizedSecurityCode) && (length == 3
        || length == 4);
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

  public static String getCardNumberRawValue(String text) {
    return text.replace(DIGIT_SEPARATOR + "", "");
  }

  public static boolean isValidExpiryDate(ExpiryDate expiryDate) {

    if (dateExists(expiryDate)) {
      final Calendar expiryDateCalendar = getExpiryCalendar(expiryDate);

      final Calendar maxFutureCalendar = GregorianCalendar.getInstance();
      maxFutureCalendar.add(Calendar.YEAR, MAXIMUM_YEARS_IN_FUTURE);

      final Calendar maxPastCalendar = GregorianCalendar.getInstance();
      maxPastCalendar.add(Calendar.MONTH, -MAXIMUM_EXPIRED_MONTHS);

      // higher than maxPast and lower than maxFuture
      return expiryDateCalendar.compareTo(maxPastCalendar) >= 0
          && expiryDateCalendar.compareTo(maxFutureCalendar) <= 0;
    }

    return false;
  }

  public static ExpiryDate getDate(String date) {
    final SimpleDateFormat mDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT);
    final String normalizedExpiryDate = StringUtil.normalize(date);
    try {
      final Date parsedDate = mDateFormat.parse(normalizedExpiryDate);
      final Calendar calendar = GregorianCalendar.getInstance();
      calendar.setTime(parsedDate);
      // GregorianCalendar is 0 based
      return new ExpiryDate(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    } catch (ParseException e) {
      return ExpiryDate.EMPTY_DATE;
    }
  }

  /**
   * Set an {@link ExpiryDate} to be displayed on the field.
   *
   * @param expiryDate The new value.
   */
  public static String getStringDate(ExpiryDate expiryDate) {
    final SimpleDateFormat mDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT);
    if (expiryDate != null && expiryDate != ExpiryDate.EMPTY_DATE) {
      final Calendar calendar = GregorianCalendar.getInstance();
      calendar.clear();
      // first day of month, GregorianCalendar month is 0 based.
      calendar.set(expiryDate.getExpiryYear(), expiryDate.getExpiryMonth() - 1, 1);
      return mDateFormat.format(calendar.getTime());
    } else {
      return "";
    }
  }

  public static boolean isShortenCardNumber(String cardNumber) {
    return cardNumber.contains("•");
  }
}

