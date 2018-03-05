package com.asf.appcoins.sdk.util;

import android.net.Uri;
import java.math.BigDecimal;
import java.util.Formatter;

/**
 * Created by neuro on 05-03-2018.
 */

public final class UriBuilder {

  private UriBuilder() {
  }

  public static String buildUriString(String contractAddress, BigDecimal amount,
      String developerAddress, String oemAddress, String storeAddress, int networkId) {

    StringBuilder stringBuilder = new StringBuilder(4);
    try (Formatter formatter = new Formatter(stringBuilder)) {
      formatter.format(
          "ethereum:%s@%d/buy?uint256=%s&developerAddress=%s&oemAddress=%s&storeAddress=%s",
          contractAddress, networkId, amount.toString(), developerAddress, oemAddress,
          storeAddress);
    }

    return stringBuilder.toString();
  }

  public static Uri buildUri(String contractAddress, int networkId, BigDecimal amount,
      String developerAddress, String oemAddress, String storeAddress) {
    return Uri.parse(
        buildUriString(contractAddress, amount, developerAddress, oemAddress, storeAddress,
            networkId));
  }
}
