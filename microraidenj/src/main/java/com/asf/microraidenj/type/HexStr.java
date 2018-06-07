package com.asf.microraidenj.type;

import java.math.BigInteger;
import java.util.Objects;
import org.spongycastle.util.encoders.Hex;

public class HexStr {

  private final String value;

  public HexStr(String value) {
    if (value.isEmpty()) {
      throw new RuntimeException("Value cannot be empty!");
    }

    value = value.toLowerCase();

    if (value.startsWith("0x")) {
      this.value = value.substring(2);
    } else {
      this.value = value;
    }
  }

  public static HexStr from(byte[] hexBytes) {
    return new HexStr(Hex.toHexString(hexBytes));
  }

  public String get() {
    return get(false);
  }

  public String get(boolean prefix) {
    return prefix ? "0x" + value : value;
  }

  public byte[] getDecoded() {
    return Hex.decode(value);
  }

  public BigInteger getAsBigInteger() {
    return new BigInteger(getDecoded());
  }

  @Override public int hashCode() {
    return value.hashCode();
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HexStr hexStr = (HexStr) o;
    return Objects.equals(value, hexStr.value);
  }
}
