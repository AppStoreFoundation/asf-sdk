package com.bds.microraidenj;

import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.channel.BDSChannel;
import com.bds.microraidenj.ws.ChannelHistoryResponse;
import ethereumj.crypto.ECKey;
import io.reactivex.Single;
import java.math.BigInteger;
import java.util.List;

public interface MicroRaidenBDS {

  Single<BDSChannel> createChannel(ECKey senderECKey, Address receiverAddress, BigInteger balance);

  Single<List<BDSChannel>> listChannels(ECKey senderECKey, boolean closed);

  Single<List<ChannelHistoryResponse.MicroTransaction>> listTransactions(Address senderAddress);
}
