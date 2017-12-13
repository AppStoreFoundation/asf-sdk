package cm.aptoide.pt.microraidenj;

import java.math.BigDecimal;

public interface MicroProof {

  BigDecimal getBalance();

  void setBalance(BigDecimal balance);

  byte[] getSign();
}
