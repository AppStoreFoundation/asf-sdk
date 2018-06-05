package com.asf.microraidenj.type;

import java.util.Arrays;
import org.spongycastle.util.encoders.Hex;

public class ByteArray {

  private final byte[] bytes;

  public ByteArray(byte[] bytes) {
    this.bytes = bytes;
  }

  public static ByteArray from(String hex) {
    if (hex.startsWith("0x")) {
      return new ByteArray(Hex.decode(hex.substring(2)));
    } else {
      return new ByteArray(Hex.decode(hex));
    }
  }

  public final String toHexString() {
    return toHexString(false);
  }

  public final String toHexString(boolean withPrefix) {
    return withPrefix ? "0x" + Hex.toHexString(bytes) : Hex.toHexString(bytes);
  }

  public final byte[] getBytes() {
    return bytes;
  }

  @Override public String toString() {
    return toHexString(true);
  }

  @Override public final int hashCode() {
    return Arrays.hashCode(bytes);
  }

  @Override public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ByteArray byteArray = (ByteArray) o;
    return Arrays.equals(bytes, byteArray.bytes);
  }
}
