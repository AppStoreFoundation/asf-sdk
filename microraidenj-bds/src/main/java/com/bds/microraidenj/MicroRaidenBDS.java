package com.bds.microraidenj;

import com.asf.microraidenj.MicroRaidenClient;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.channel.BDSChannelClient;
import com.bds.microraidenj.channel.BDSChannelClientImpl;
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

  public Single<BDSChannelClient> createChannel(ECKey senderECKey, Address receiverAddress,
      BigInteger balance) {
    return Single.fromCallable(
        () -> microRaidenClient.createChannel(senderECKey, receiverAddress, balance))
        .map(openBlockNumber -> new BDSChannelClientImpl(senderECKey, receiverAddress,
            openBlockNumber, microRaidenClient, bdsMicroRaidenApi, BigInteger.ZERO, balance));
  }
}
