package com.sdk.appcoins_adyen.methods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class with a list of all the currently supported Payment Methods on Components and
 * Drop-In.
 */
public final class PaymentMethodTypes {

  // Type of the payment method as received by the paymentMethods/ API
  public static final String SCHEME = "scheme";

  // List of all payment method types.
  public static final List<String> SUPPORTED_PAYMENT_METHODS;
  public static final List<String> UNSUPPORTED_PAYMENT_METHODS;

  static {
    final ArrayList<String> supportedPaymentMethods = new ArrayList<>();

    // Populate supported list
    supportedPaymentMethods.add(SCHEME);

    SUPPORTED_PAYMENT_METHODS = Collections.unmodifiableList(supportedPaymentMethods);

    final ArrayList<String> unsupportedPaymentMethods = new ArrayList<>();

    UNSUPPORTED_PAYMENT_METHODS = Collections.unmodifiableList(unsupportedPaymentMethods);
  }

  private PaymentMethodTypes() {
  }
}
