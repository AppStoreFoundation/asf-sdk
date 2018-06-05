package com.bds.microraidenj;

import com.asf.microraidenj.MicroRaidenClient;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.ws.BDSMicroRaidenApi;
import com.bds.microraidenj.ws.CloseChannelResponse;
import ethereumj.crypto.ECKey;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.math.BigInteger;
import org.spongycastle.util.encoders.Hex;

public final class MicroRaidenBDS {

  private final MicroRaidenClient microRaidenClient;
  private final BDSMicroRaidenApi bdsMicroRaidenApi;

  public MicroRaidenBDS(MicroRaidenClient microRaidenClient, BDSMicroRaidenApi bdsMicroRaidenApi) {
    this.microRaidenClient = microRaidenClient;
    this.bdsMicroRaidenApi = bdsMicroRaidenApi;
  }

  public Single<BigInteger> createChannel(ECKey senderECKey, Address receiverAddress,
      BigInteger owedBalance) {
    return Single.fromCallable(
        () -> microRaidenClient.createChannel(senderECKey, receiverAddress, owedBalance));
  }

  public Completable topUpChannel(ECKey senderECKey, Address receiverAddress,
      BigInteger openBlockNumber, BigInteger deposit) {
    return Completable.fromAction(
        () -> microRaidenClient.topUpChannel(senderECKey, receiverAddress, openBlockNumber,
            deposit));
  }

  public Single<String> closeChannel(ECKey senderECKey, Address receiverAddress,
      BigInteger openBlockNumber, BigInteger owedBalance) {

    return Single.fromCallable(() -> Hex.toHexString(
        microRaidenClient.createBalanceProof(senderECKey, receiverAddress, openBlockNumber,
            owedBalance)))
        .flatMapObservable(
            balanceProof -> bdsMicroRaidenApi.closeChannel(Address.from(senderECKey.getAddress()),
                openBlockNumber, owedBalance))
        .map(this::extractSig)
        .map(closingMsg -> microRaidenClient.closeChannelCooperatively(senderECKey, receiverAddress,
            openBlockNumber, owedBalance, closingMsg, senderECKey))
        .singleOrError();
  }

  private byte[] extractSig(CloseChannelResponse closeChannelResponse) {
    return Hex.decode(closeChannelResponse.getClosingSig());
  }
}
