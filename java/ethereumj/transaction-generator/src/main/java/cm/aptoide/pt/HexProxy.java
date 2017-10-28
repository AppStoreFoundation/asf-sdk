package cm.aptoide.pt;

import org.spongycastle.util.encoders.Hex;

public class HexProxy {

	static byte[] decode(String s) {
		if (s.length() % 2 != 0) {
			s = " " + s;
		}

		return Hex.decode(s);
	}
}
