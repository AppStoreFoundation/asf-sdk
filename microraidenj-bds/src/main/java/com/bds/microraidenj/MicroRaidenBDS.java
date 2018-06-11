package com.bds.microraidenj;

import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.channel.BDSChannelClient;
import ethereumj.crypto.ECKey;
import io.reactivex.Single;
import java.math.BigInteger;
import java.util.List;

public interface MicroRaidenBDS {

  Single<BDSChannelClient> createChannel(ECKey senderECKey, Address receiverAddress,
      BigInteger balance);

  Single<List<BDSChannelClient>> listChannels(Address senderAddress, Address receiverAddress);
}
