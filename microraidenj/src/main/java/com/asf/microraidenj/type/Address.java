package com.asf.microraidenj.type;

public class Address {

  private final HexStr addr;

  public Address(HexStr addr) {
    this.addr = addr;
  }

  public static Address from(byte[] address) {
    return new Address(HexStr.from(address));
  }

  public static Address from(String address) {
    return new Address(new HexStr(address));
  }

  public HexStr getHexaStr() {
    return addr;
  }
}
