package com.asf.microraidenj.type;

public class Address extends ByteArray {

  public Address(ByteArray byteArray) {
    super(byteArray.getBytes());
  }

  public static Address from(byte[] address) {
    return new Address(new ByteArray(address));
  }

  public static Address from(String address) {
    return new Address(ByteArray.from(address));
  }

  @Override public String toString() {
    return toHexString(true);
  }
}
