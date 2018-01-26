package cm.aptoide.pt.ethereum;

import org.spongycastle.util.encoders.Hex;

import cm.aptoide.pt.ethereumj.util.ByteUtil;

public class HexUtils {

	static String fromPrefixString(String hexWithPrefix) {
		return hexWithPrefix.substring(2, hexWithPrefix.length());
	}

	static byte[] decode(String s) {
		if (s.charAt(0) == '0') {
			return ByteUtil.ZERO_BYTE_ARRAY;
		}

		if (s.length() % 2 != 0) {
			s = " " + s;
		}

		return Hex.decode(s);
	}
}
