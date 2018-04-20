package com.asf.appcoins.sdk.iab.util;

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

  public static String buildUriString(String tokenContractAddress, String iabContractAddress,
      BigDecimal amount, String developerAddress, String skuId, int networkId) {

    StringBuilder stringBuilder = new StringBuilder(4);
    try (Formatter formatter = new Formatter(stringBuilder)) {
      formatter.format("ethereum:%s@%d/buy?uint256=%s&address=%s&data=%s&iabContractAddress=%s",
          tokenContractAddress, networkId, amount.toString(), developerAddress,
          "0x" + Hex.toHexString(skuId.getBytes("UTF-8")), iabContractAddress);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("UTF-8 not supported!", e);
    }

    return stringBuilder.toString();
  }
}
