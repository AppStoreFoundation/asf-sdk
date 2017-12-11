package cm.aptoide.pt.microraidenj;

import android.support.annotation.Nullable;

/**
 * MicroRaiden.channel data structure
 */
public interface MicroChannel {

  String getAccount();

  String getReceiver();

  Number getBlock();

  MicroProof getProof();

  @Nullable MicroProof nextProofget();

  @Nullable String closeSignget();
}
