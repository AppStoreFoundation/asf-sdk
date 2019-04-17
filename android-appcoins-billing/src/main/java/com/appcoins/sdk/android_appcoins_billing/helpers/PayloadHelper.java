package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.net.Uri;
import com.appcoins.billing.AppcoinsBilling;

/**
 * Intent payload helper class that provide a way to send the developers wallet address together
 * with an already existent developers payload when using {@link AppcoinsBilling#getBuyIntent}.
 *
 * The use of this helper is mandatory even if there is no  existing payload, because it allows for
 * a payment to be delivered to the developers ethereum address.
 *
 * This class must be imported to your project and used without any changes to be compatible with
 * the Appcoins billing process.
 */
public class PayloadHelper {
  private static final String SCHEME = "appcoins";
  private static final String PAYLOAD_PARAMETER = "payload";
  private static final String ORDER_PARAMETER = "order_reference";
  private static final String ORIGIN_PARAMETER = "origin";

  /**
   * Method to build the payload required on the {@link AppcoinsBilling#getBuyIntent} method.
   *
   * @param developerPayload The additional payload to be sent
   * @param origin payment origin (BDS, UNITY,EXTERNAL)
   * @param orderReference a reference that allows the developers to identify this order in
   * server-to-server communication
   *
   * @return The final developers payload to be sent
   */
  public static String buildIntentPayload(final String orderReference,
      final String developerPayload, String origin) {
    Uri.Builder builder = new Uri.Builder();
    builder.scheme(SCHEME)
        .authority("appcoins.io");
    if (developerPayload != null) {
      builder.appendQueryParameter(PAYLOAD_PARAMETER, developerPayload);
    }
    if (orderReference != null) {
      builder.appendQueryParameter(ORDER_PARAMETER, orderReference);
    }
    if (origin != null) {
      builder.appendQueryParameter(ORIGIN_PARAMETER, origin);
    }
    return builder.toString();
  }
}