package cm.aptoide.pt;

import org.spongycastle.util.encoders.Hex;

public class HexUtils {

	static String fromPrefixString(String hexWithPrefix) {
		return hexWithPrefix.substring(2, hexWithPrefix.length());
	}

	static byte[] decode(String s) {
		if (s.charAt(0) == '0') {
			return new byte[]{0};
		}

		if (s.length() % 2 != 0) {
			s = " " + s;
		}

		return Hex.decode(s);
	}
}
