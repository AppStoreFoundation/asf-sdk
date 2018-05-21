package com.asf.microraidenj.type;

import java.math.BigInteger;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AddressTest {

  private static Address address;
  private static HexStr addrHexStr;

  @BeforeClass public static void beforeClass() {
    addrHexStr = new HexStr("0xd95c64c6eee9164539d679354f349779a04f57cb");
    address = new Address(addrHexStr);
  }

  @Test public void from() {
    HexStr hexStr = new HexStr("795c64c6eee9164539d679354f349779a04f57cb");
    byte[] addressBytes = new BigInteger(
        "111100101011100011001001100011011101110111010010001011001000101001110011101011001111001001101010100111100110100100101110111100110100000010011110101011111001011",
        2).toByteArray();

    Address address;

    address = Address.from(hexStr.get());
    assertThat(address.getHexaStr(), is(hexStr));

    address = Address.from(hexStr.getDecoded());
    assertThat(address.getHexaStr(), is(hexStr));
  }

  @Test public void get() {
    assertThat(address.getHexaStr(), is(addrHexStr));
  }
}