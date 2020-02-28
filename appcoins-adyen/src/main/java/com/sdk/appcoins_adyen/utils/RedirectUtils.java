package com.sdk.appcoins_adyen.utils;

import android.content.Context;
import android.net.Uri;
import com.sdk.appcoins_adyen.exceptions.CheckoutException;
import org.json.JSONException;
import org.json.JSONObject;

public class RedirectUtils {

  public static final String REDIRECT_RESULT_SCHEME = "adyencheckout://";
  /**
   * The suggested scheme to be used in the intent filter to receive the redirect result.
   * This value should be the beginning of the `returnUr` sent on the payments/ call.
   */

  private static final String PAYLOAD_PARAMETER = "payload";
  private static final String REDIRECT_RESULT_PARAMETER = "redirectResult";
  private static final String PAYMENT_RESULT_PARAMETER = "PaRes";
  private static final String MD_PARAMETER = "MD";

  public static String getReturnUrl(Context context) {
    return REDIRECT_RESULT_SCHEME + context.getPackageName();
  }

  public static JSONObject parseRedirectResult(Uri data) throws CheckoutException {

    final JSONObject result = new JSONObject();

    for (String parameter : data.getQueryParameterNames()) {
      // getQueryParameter already does HTML decoding
      if (PAYLOAD_PARAMETER.equals(parameter)) {
        try {
          result.put(PAYLOAD_PARAMETER, data.getQueryParameter(parameter));
        } catch (JSONException e) {
          throw new CheckoutException("Error creating Redirect payload.", e);
        }
      }
      if (REDIRECT_RESULT_PARAMETER.equals(parameter)) {
        try {
          result.put(REDIRECT_RESULT_PARAMETER, data.getQueryParameter(parameter));
        } catch (JSONException e) {
          throw new CheckoutException("Error creating Redirect result parameter.", e);
        }
      }
      if (PAYMENT_RESULT_PARAMETER.equals(parameter)) {
        try {
          result.put(PAYMENT_RESULT_PARAMETER, data.getQueryParameter(parameter));
        } catch (JSONException e) {
          throw new CheckoutException("Error creating Redirect payment result.", e);
        }
      }
      if (MD_PARAMETER.equals(parameter)) {
        try {
          result.put(MD_PARAMETER, data.getQueryParameter(parameter));
        } catch (JSONException e) {
          throw new CheckoutException("Error creating Redirect MD.", e);
        }
      }
    }

    return result;
  }
}
