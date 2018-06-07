package com.asf.microraidenj.type;

public class Address extends HexStr {

  public Address(String value) {
    super(value);
  }

  public static Address from(byte[] address) {
    return new Address(HexStr.from(address)
        .get());
  }

  public static Address from(String address) {
    return new Address(address);
  }
}
