package com.asf.microraidenj.type;

import java.math.BigInteger;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ByteArrayTest {

  private static byte[] bytes;
  private static ByteArray byteArray;

  @BeforeClass public static void initClass() {
    bytes = new BigInteger(
        "111100101011100011001001100011011101110111010010001011001000101001110011101011001111001001101010100111100110100100101110111100110100000010011110101011111001011",
        2).toByteArray();
    byteArray = new ByteArray(bytes);
  }

  @Test public void from() {
    ByteArray from = ByteArray.from("795c64c6eee9164539d679354f349779a04f57cb");
    assertThat(from, is(byteArray));
  }

  @Test public void toHexString() {
    assertThat(byteArray.toHexString(), is("795c64c6eee9164539d679354f349779a04f57cb"));
  }

  @Test public void toHexStringWithFlag() {
    assertThat(byteArray.toHexString(true), is("0x795c64c6eee9164539d679354f349779a04f57cb"));
    assertThat(byteArray.toHexString(false), is("795c64c6eee9164539d679354f349779a04f57cb"));
  }

  @Test public void getBytes() {
    assertThat(byteArray.getBytes(), is(bytes));
  }
}