package com.sdk.appcoins_adyen.exceptions;

import com.sdk.appcoins_adyen.utils.ModelUtils;

/**
 * +
 * Exception thrown when a {@link com.sdk.appcoins_adyen.api.ModelObject} does not meet the
 * requirement of having a SERIALIZER object.
 */
public class BadModelException extends CheckoutException {

  private static final long serialVersionUID = -1161500360463809921L;

  public BadModelException(Class<?> clazz, Throwable e) {
    super("ModelObject protocol requires a ModelObject.Serializer object called "
        + ModelUtils.SERIALIZER_FIELD_NAME
        + " on class "
        + clazz.getSimpleName(), e);
  }
}