package com.asf.microraidenj.type;

import java.math.BigInteger;
import java.util.Arrays;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class HexStrTest {

  private static HexStr zeroHex;
  private static HexStr ffHex;
  private final String addressString1 = "795c64c6eee9164539d679354f349779a04f57cb";
  private final String addressString2 = "895c64c6eee9164539d679354f349779a04f57cb";

  @BeforeClass public static void beforeClass() {
    zeroHex = HexStr.from("0x00");
    ffHex = HexStr.from("FF");
  }

  @Test public void constructor() {

    RuntimeException exception = null;

    try {
      HexStr.from("");
    } catch (RuntimeException e) {
      exception = e;
    }

    assertNotNull(exception);
  }

  @Test public void getEncoded() {
    assertThat(zeroHex.get(true), is("0x00"));
    assertThat(ffHex.get(true), is("0xff"));

    assertThat(zeroHex.get(), is("00"));
    assertThat(ffHex.get(), is("ff"));
  }

  @Test public void getDecoded() {
    byte[] arr = new byte[1];
    Arrays.fill(arr, (byte) 0);

    assertThat(zeroHex.getDecoded(), is(arr));

    Arrays.fill(arr, (byte) 255);

    assertThat(ffHex.getDecoded(), is(arr));
  }

  @Test public void getAsBigInteger() {
    assertThat(zeroHex.getAsBigInteger(), is(BigInteger.ZERO));
    assertThat(ffHex.getAsBigInteger(), is(BigInteger.valueOf((byte) 255)));
  }

  @Test public void from() {
    byte[] hexBytes = new BigInteger(
        "111100101011100011001001100011011101110111010010001011001000101001110011101011001111001001101010100111100110100100101110111100110100000010011110101011111001011",
        2).toByteArray();

    HexStr hexStr;

    hexStr = HexStr.from(hexBytes);
    assertThat(hexStr.getDecoded(), is(hexBytes));
  }

  @Test public void equalsAndhashCode() {
    HexStr hexStr = HexStr.from(addressString1);

    assertThat(hexStr, is(HexStr.from(addressString1)));
    assertThat(hexStr.hashCode(), is(HexStr.from(addressString1)
        .hashCode()));

    assertThat(hexStr, not(HexStr.from(addressString2)));
    assertThat(hexStr.hashCode(), not(HexStr.from(addressString2)
        .hashCode()));
  }
}