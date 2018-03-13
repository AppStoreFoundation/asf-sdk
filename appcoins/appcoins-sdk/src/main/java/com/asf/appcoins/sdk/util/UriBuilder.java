package com.asf.appcoins.sdk.util;

import android.net.Uri;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Formatter;
import org.spongycastle.util.encoders.Hex;

/**
 * Created by neuro on 05-03-2018.
 */

public final class UriBuilder {

  private UriBuilder() {
  }

  public static String buildUriString(String contractAddress, BigDecimal amount,
      String developerAddress, String skuId, int networkId) {

    StringBuilder stringBuilder = new StringBuilder(4);
    try (Formatter formatter = new Formatter(stringBuilder)) {
      formatter.format("ethereum:%s@%d/buy?uint256=%s&developerAddress=%s&data=%s",
          contractAddress, networkId, amount.toString(), developerAddress,
          "0x" + Hex.toHexString(skuId.getBytes("UTF-8")));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("UTF-8 not supported!", e);
    }

    return stringBuilder.toString();
  }

  public static Uri buildUri(String contractAddress, String skuId, int networkId, BigDecimal amount,
      String developerAddress) {
    return Uri.parse(buildUriString(contractAddress, amount, developerAddress, skuId, networkId));
  }
}
