package com.sdk.appcoins_adyen.utils;

import android.os.Parcel;

public final class ParcelUtils {

  public static final int NO_FILE_DESCRIPTOR = 0;

  private static final int BOOLEAN_TRUE_VALUE = 1;
  private static final int BOOLEAN_FALSE_VALUE = 0;

  private ParcelUtils() {
  }

  /**
   * Write boolean in to Parcel.
   */
  public static void writeBoolean(Parcel dest, boolean value) {
    dest.writeInt(value ? BOOLEAN_TRUE_VALUE : BOOLEAN_FALSE_VALUE);
  }

  /**
   * Read boolean from Parcel.
   */
  public static boolean readBoolean(Parcel in) {
    return in.readInt() == BOOLEAN_TRUE_VALUE;
  }
}