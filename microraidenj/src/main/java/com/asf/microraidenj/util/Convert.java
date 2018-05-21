package com.asf.microraidenj.util;

import java.math.BigInteger;

public class Convert {
  private Convert() {
  }

  /**
   * Convert BigInteger to the byte array used as arguments of calling smart contract functions.
   *
   * @param value the BigInteger to be changed to byte array.
   *
   * @return the byte array sent to contract functions.
   */
  public static byte[] bigIntegerToBytes(BigInteger value) {
    if (value == null) return null;

    byte[] data = value.toByteArray();

    if (data.length != 1 && data[0] == 0) {
      byte[] tmp = new byte[data.length - 1];
      System.arraycopy(data, 1, tmp, 0, tmp.length);
      data = tmp;
    }
    return data;
  }
}
