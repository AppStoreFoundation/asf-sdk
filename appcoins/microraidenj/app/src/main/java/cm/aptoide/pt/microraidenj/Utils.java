package cm.aptoide.pt.microraidenj;

import java.nio.charset.Charset;
import org.spongycastle.jcajce.provider.digest.SHA3;
import org.spongycastle.util.encoders.Hex;

public class Utils {

  private static final String TAG = Utils.class.getSimpleName();

  static double num2bal(double value, double decimals) {
    return Math.floor(value * Math.pow(10, decimals));
  }

  /**
   * Encode a string or number as hexadecimal, without '0x' prefix.
   *
   * @param str string representing hex to encode.
   *
   * @return encoded hex.
   */
  static byte[] encodeHex(String str) {
    return Hex.encode(str.getBytes(Charset.defaultCharset()));
  }

  static String sha3Hash(byte[] hex) {
    SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
    byte[] digest = digestSHA3.digest(hex);

    return Hex.toHexString(digest);
  }
}
