package com.bds.microraidenj.channel;

import com.asf.microraidenj.MicroRaidenClient;
import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.ws.BDSMicroRaidenApi;
import com.bds.microraidenj.ws.CloseChannelResponse;
import ethereumj.crypto.ECKey;
import io.reactivex.Single;
import java.math.BigInteger;
import org.spongycastle.util.encoders.Hex;

public final class ChannelClientImpl implements ChannelClient {

  private final BigInteger openBlockNumber;
  private final ECKey senderECKey;
  private final Address receiverAddress;
  private final MicroRaidenClient microRaidenClient;
  private final BDSMicroRaidenApi bdsMicroRaidenApi;

  private BigInteger owedBalance;
  private BigInteger totalBalance;

  public ChannelClientImpl(ECKey senderECKey, Address receiverAddress, BigInteger openBlockNumber,
      BigInteger owedBalance, BigInteger totalBalance, MicroRaidenClient microRaidenClient,
      BDSMicroRaidenApi bdsMicroRaidenApi) {
    this.senderECKey = senderECKey;
    this.receiverAddress = receiverAddress;
    this.openBlockNumber = openBlockNumber;
    this.microRaidenClient = microRaidenClient;

    this.owedBalance = owedBalance;
    this.totalBalance = totalBalance;
    this.bdsMicroRaidenApi = bdsMicroRaidenApi;
  }

  public ChannelClientImpl(ECKey senderECKey, Address receiverAddress, BigInteger openBlockNumber,
      BigInteger balance, MicroRaidenClient microRaidenClient,
      BDSMicroRaidenApi bdsMicroRaidenApi) {
    this(senderECKey, receiverAddress, openBlockNumber, BigInteger.ZERO, balance, microRaidenClient,
        bdsMicroRaidenApi);
  }

  @Override public void topUp(BigInteger depositToAdd)
      throws TransactionFailedException, DepositTooHighException {
    microRaidenClient.topUpChannel(senderECKey, receiverAddress, openBlockNumber, depositToAdd);
  }

  @Override public String closeCooperatively(BigInteger owedBalance, ECKey ecKey) {
    return bdsMicroRaidenApi.closeChannel(getSenderAddress(), openBlockNumber, owedBalance)
        .map(CloseChannelResponse::getClosingSig)
        .map(closingSig -> Hex.decode(closingSig.substring(2)))
        .map(closingSigBytes -> microRaidenClient.closeChannelCooperatively(ecKey, receiverAddress,
            openBlockNumber, owedBalance, closingSigBytes, senderECKey))
        .blockingFirst();
  }

  @Override public Single<byte[]> transfer(BigInteger amount) {
    return Single.fromCallable(() -> {
      if (hasFunds(amount)) {
        byte[] balanceMsgHash =
            microRaidenClient.createBalanceProof(senderECKey, receiverAddress, openBlockNumber,
                owedBalance);

        return balanceMsgHash;
      } else {
        throw new InsufficientFundsException(
            "Insuficient funds! Available: " + getBalance() + ", Requested: " + amount);
      }
    })
        .doOnSuccess(balanceMsgHash -> owedBalance = owedBalance.add(amount));
  }

  @Override public BigInteger getOpenBlockNumber() {
    return openBlockNumber;
  }

  @Override public BigInteger getBalance() {
    return totalBalance.subtract(owedBalance);
  }

  @Override public Address getSenderAddress() {
    return Address.from(senderECKey.getAddress());
  }

  @Override public Address getReceiverAddress() {
    return receiverAddress;
  }

  private boolean hasFunds(BigInteger amount) {
    return (getBalance().subtract(amount)
        .compareTo(BigInteger.ZERO)) > 0;
  }
}
