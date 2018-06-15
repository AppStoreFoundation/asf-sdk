package com.bds.microraidenj;

import com.asf.microraidenj.MicroRaidenClient;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.channel.ChannelClient;
import com.bds.microraidenj.channel.ChannelClientImpl;
import com.bds.microraidenj.ws.BDSMicroRaidenApi;
import ethereumj.crypto.ECKey;
import io.reactivex.Single;
import java.math.BigInteger;

public final class MicroRaidenBDS {

  private final MicroRaidenClient microRaidenClient;
  private final BDSMicroRaidenApi bdsMicroRaidenApi;

  public MicroRaidenBDS(MicroRaidenClient microRaidenClient, BDSMicroRaidenApi bdsMicroRaidenApi) {
    this.microRaidenClient = microRaidenClient;
    this.bdsMicroRaidenApi = bdsMicroRaidenApi;
  }

  public Single<ChannelClient> createChannel(ECKey senderECKey, Address receiverAddress,
      BigInteger balance) {
    return Single.fromCallable(
        () -> microRaidenClient.createChannel(senderECKey, receiverAddress, balance))
        .map(openBlockNumber -> new ChannelClientImpl(senderECKey, receiverAddress, openBlockNumber,
            BigInteger.ZERO, balance, microRaidenClient, bdsMicroRaidenApi));
  }
}
