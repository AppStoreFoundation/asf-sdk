package com.bds.microraidenj;

import com.asf.microraidenj.MicroRaidenClient;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.channel.BDSChannel;
import com.bds.microraidenj.channel.BDSChannelImpl;
import com.bds.microraidenj.ws.BDSMicroRaidenApi;
import com.bds.microraidenj.ws.ChannelHistoryResponse;
import com.bds.microraidenj.ws.ListAllChannelsResponse;
import com.bds.microraidenj.ws.Type;
import ethereumj.crypto.ECKey;
import io.reactivex.Single;
import java.math.BigInteger;
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
        .map(openBlockNumber -> new BDSChannelImpl(senderECKey, receiverAddress, openBlockNumber,
            microRaidenClient, bdsMicroRaidenApi, BigInteger.ZERO, balance))
        .flatMap(
            bdsChannel -> bdsMicroRaidenApi.listAllChannels(Address.from(senderECKey.getAddress()),
                false)
                .flatMapIterable(ListAllChannelsResponse::getResult)
                .map(ListAllChannelsResponse.Result::getBlock)
                .contains(bdsChannel.getOpenBlockNumber()
                    .intValue())
                .doOnSuccess(aBoolean -> {
                  if (!aBoolean) {
                    throw new RuntimeException();
                  }
                })
                .retry()
                .map(aBoolean -> bdsChannel));
  }

  @Override public Single<List<BDSChannel>> listChannels(ECKey senderECKey, boolean closed) {
    return bdsMicroRaidenApi.listAllChannels(Address.from(senderECKey.getAddress()), closed)
        .flatMapIterable(ListAllChannelsResponse::getResult)
        .map(result -> map(senderECKey, result))
        .toList();
  }

  @Override public Single<List<ChannelHistoryResponse.MicroTransaction>> listTransactions(
      Address senderAddress) {
    return bdsMicroRaidenApi.channelHistory(senderAddress, null, null, Type.Payment)
        .map(ChannelHistoryResponse::getResult)
        .singleOrError();
  }

  private BDSChannel map(ECKey senderECKey, ListAllChannelsResponse.Result result) {
    Address receiverAddress = Address.from(result.getReceiver());
    BigInteger block = BigInteger.valueOf(result.getBlock());
    BigInteger owedBalance = result.getReceiverBalance();
    BigInteger balance = result.getSenderBalance();

    BigInteger totalBalance = owedBalance.add(balance);

    return new BDSChannelImpl(senderECKey, receiverAddress, block, microRaidenClient,
        bdsMicroRaidenApi, owedBalance, totalBalance);
  }
}
