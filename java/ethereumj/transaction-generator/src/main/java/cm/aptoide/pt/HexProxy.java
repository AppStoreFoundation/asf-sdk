package cm.aptoide.pt;

import org.spongycastle.util.encoders.Hex;

public class HexProxy {

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
