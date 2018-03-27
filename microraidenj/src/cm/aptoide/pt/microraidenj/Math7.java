package cm.aptoide.pt.microraidenj;

/**
 * Created by neuro on 31-01-2018.
 */

public class Math7 {

  /**
   * Returns the value of the {@code long} argument;
   * throwing an exception if the value overflows an {@code int}.
   *
   * @param value the long value
   *
   * @return the argument as an int
   *
   * @throws ArithmeticException if the {@code argument} overflows an int
   * @since 1.8
   */
  public static int toIntExact(long value) {
    if ((int) value != value) {
      throw new ArithmeticException("integer overflow");
    }
    return (int) value;
  }
}
