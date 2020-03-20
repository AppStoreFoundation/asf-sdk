package com.sdk.appcoins_adyen.card;

public class ValidatedField<T> {

  private final T mValue;
  private final Validation mValidation;

  public ValidatedField(T value, Validation validation) {
    mValue = value;
    mValidation = validation;
  }

  public boolean isValid() {
    return mValidation == Validation.VALID;
  }

  public T getValue() {
    return mValue;
  }

  public ValidatedField.Validation getValidation() {
    return mValidation;
  }

  public enum Validation {
    VALID, PARTIAL, INVALID
  }
}