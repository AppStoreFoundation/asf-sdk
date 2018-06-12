package com.bds.microraidenj;

import com.asf.microraidenj.MicroRaidenClient;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.channel.BDSChannel;
import com.bds.microraidenj.channel.BDSChannelImpl;
import com.bds.microraidenj.ws.BDSMicroRaidenApi;
import ethereumj.crypto.ECKey;
import io.reactivex.Single;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public final class DefaultMicroRaidenBDS implements MicroRaidenBDS {

  private final MicroRaidenClient microRaidenClient;
  private final BDSMicroRaidenApi bdsMicroRaidenApi;

  public DefaultMicroRaidenBDS(MicroRaidenClient microRaidenClient,
      BDSMicroRaidenApi bdsMicroRaidenApi) {
    this.microRaidenClient = microRaidenClient;
    this.bdsMicroRaidenApi = bdsMicroRaidenApi;
  }

  @Override public Single<BDSChannel> createChannel(ECKey senderECKey, Address receiverAddress,
      BigInteger balance) {
    return Single.fromCallable(
        () -> microRaidenClient.createChannel(senderECKey, receiverAddress, balance))
        .map(openBlockNumber -> new BDSChannelImpl(senderECKey, receiverAddress,
            openBlockNumber, microRaidenClient, bdsMicroRaidenApi, BigInteger.ZERO, balance));
  }

  @Override public Single<List<BDSChannel>> listChannels(Address senderAddress,
      Address receiverAddress) {
    return Single.just(Collections.emptyList());
  }
}
