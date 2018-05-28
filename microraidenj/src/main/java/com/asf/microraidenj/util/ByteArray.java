package com.asf.microraidenj.util;

public class ByteArray {

  private ByteArray() {
  }

  public static byte[] prependZeros(byte[] arr, int size) {

    if (arr.length > size) {
      throw new IllegalArgumentException("Size cannot be bigger than array size!");
    }

    byte[] bytes = new byte[size];
    System.arraycopy(arr, 0, bytes, (bytes.length - arr.length), arr.length);

    return bytes;
  }
}
