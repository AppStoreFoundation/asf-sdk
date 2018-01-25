package cm.aptoide.pt.microraidenj;

import java.math.BigDecimal;

/**
 * MicroRaiden.getTokenInfo result
 */
public interface MicroTokenInfo {
  String getName();

  String getSymbol();

  Number getDecimals();

  BigDecimal getBalance();
}
