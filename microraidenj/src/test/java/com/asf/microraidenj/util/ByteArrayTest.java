package com.asf.microraidenj.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public final class ByteArrayTest {

  @Test public void prependZeros() {
    byte[] bytes = new byte[2];
    bytes[0] = 'a';
    bytes[1] = 'b';

    byte[] expected = new byte[8];
    expected[6] = 'a';
    expected[7] = 'b';

    assertArrayEquals(ByteArray.prependZeros(bytes, 8), expected);
  }
}