package cm.aptoide.pt.microraidenj;

import java.math.BigDecimal;

/**
 * MicroRaiden.getChannelInfo result
 */
public interface MicroChannelInfo {

  String getState();

  Number getBlock();

  BigDecimal getDeposit();
}
