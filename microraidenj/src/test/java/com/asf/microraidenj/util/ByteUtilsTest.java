package com.asf.microraidenj.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public final class ByteUtilsTest {

  @Test public void prependZeros() {
    byte[] bytes = new byte[2];
    bytes[0] = 'a';
    bytes[1] = 'b';

    byte[] expected = new byte[8];
    expected[6] = 'a';
    expected[7] = 'b';

    assertArrayEquals(expected, ByteUtils.prependZeros(bytes, 8));
  }

  @Test public void concat() {
    byte[] b1 = "a".getBytes();
    byte[] b2 = "b".getBytes();

    byte[] expected = "ab".getBytes();

    assertArrayEquals(expected, ByteUtils.concat(b1, b2));
  }
}