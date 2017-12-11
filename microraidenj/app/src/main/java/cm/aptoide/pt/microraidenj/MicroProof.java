package cm.aptoide.pt.microraidenj;

import java.math.BigDecimal;

public interface MicroProof {

  BigDecimal getBalance();

  byte[] getSign();
}
