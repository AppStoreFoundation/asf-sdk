package com.asf.microraidenj.util;

public class ByteUtils {

  private ByteUtils() {
  }

  public static byte[] prependZeros(byte[] arr, int size) {

    if (arr.length > size) {
      throw new IllegalArgumentException("Size cannot be bigger than array size!");
    }

    byte[] bytes = new byte[size];
    System.arraycopy(arr, 0, bytes, (bytes.length - arr.length), arr.length);

    return bytes;
  }

  /**
   * Helper function to join many byte arrays together.
   *
   * @param arrays arrays to merge.
   *
   * @return the merged array
   */
  public static byte[] concat(byte[]... arrays) {
    int count = 0;
    for (byte[] array : arrays) {
      count += array.length;
    }

    // Create new array and copy all array contents
    byte[] mergedArray = new byte[count];
    int start = 0;
    for (byte[] array : arrays) {
      System.arraycopy(array, 0, mergedArray, start, array.length);
      start += array.length;
    }
    return mergedArray;
  }
}
