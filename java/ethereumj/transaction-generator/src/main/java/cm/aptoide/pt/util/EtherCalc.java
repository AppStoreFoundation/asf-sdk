package cm.aptoide.pt.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class EtherCalc {

    private static final BigInteger ETH_CONS = new BigInteger("1000000000000000000");

    public String toHex(BigInteger bigInteger) {
        // TODO: 11-11-2017 neuro
        throw new IllegalStateException("Not implemented!");
    }

    public String toHex(long value) {
        return this.toHex(BigInteger.valueOf(value));
    }

    public BigDecimal toEther(String hex) {
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }

        return new BigDecimal(new BigInteger(hex, 16)).divide(new BigDecimal(EtherCalc.ETH_CONS));
    }
}
