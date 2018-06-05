package com.bds.microraidenj.channel;

import com.asf.microraidenj.MicroRaidenClient;
import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import com.asf.microraidenj.type.ByteArray;
import com.bds.microraidenj.ws.BDSMicroRaidenApi;
import com.bds.microraidenj.ws.CloseChannelResponse;
import ethereumj.crypto.ECKey;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.spongycastle.util.encoders.Hex;

public final class BDSChannelClientImpl implements BDSChannelClient {

  private final ECKey senderECKey;
  private final Address receiverAddress;
  private final BigInteger openBlockNumber;
  private final MicroRaidenClient microRaidenClient;
  private final BDSMicroRaidenApi bdsMicroRaidenApi;

  private BigInteger owedBalance;
  private BigInteger totalBalance;

  public BDSChannelClientImpl(ECKey senderECKey, Address receiverAddress,
      BigInteger openBlockNumber, MicroRaidenClient microRaidenClient,
      BDSMicroRaidenApi bdsMicroRaidenApi, BigInteger owedBalance, BigInteger totalBalance) {
    this.senderECKey = senderECKey;
    this.receiverAddress = receiverAddress;
    this.openBlockNumber = openBlockNumber;
    this.microRaidenClient = microRaidenClient;
    this.bdsMicroRaidenApi = bdsMicroRaidenApi;

    this.owedBalance = owedBalance;
    this.totalBalance = totalBalance;
  }

  public BDSChannelClientImpl(ECKey senderECKey, Address receiverAddress,
      BigInteger openBlockNumber,
      BigInteger balance, MicroRaidenClient microRaidenClient,
      BDSMicroRaidenApi bdsMicroRaidenApi) {
    this(senderECKey, receiverAddress, openBlockNumber, microRaidenClient, bdsMicroRaidenApi,
        BigInteger.ZERO, balance);
  }

  @Override public void topUp(BigInteger depositToAdd)
      throws TransactionFailedException, DepositTooHighException {
    microRaidenClient.topUpChannel(senderECKey, receiverAddress, openBlockNumber, depositToAdd);
  }

  @Override public String closeCooperatively(ECKey ecKey) {
    return Single.just(createBalanceProof())
        .map(ByteArray::new)
        .flatMapObservable(
            byteArray -> bdsMicroRaidenApi.closeChannel(getSenderAddress(), openBlockNumber,
                owedBalance, new ByteArray(createBalanceProof()))
                .map(CloseChannelResponse::getClosingSig)
                .map(closingSig -> Hex.decode(closingSig.substring(2)))
                .map(closingSigBytes -> microRaidenClient.closeChannelCooperatively(ecKey,
                    receiverAddress, openBlockNumber, owedBalance, closingSigBytes, senderECKey)))
        .blockingFirst();
  }

  @Override public void makePayment(BigInteger value, Address devAddress, Address storeAddress,
      Address oemAddress) {
    Single.fromCallable(() -> {
      if (hasFunds(value)) {
        return createBalanceProof();
      } else {
        throw new InsufficientFundsException(
            "Insuficient funds! Available: " + getBalance() + ", Requested: " + value);
      }
    })
        .doOnSubscribe(disposable -> owedBalance = owedBalance.add(value))
        .map(ByteArray::new)
        .flatMapCompletable(
            balanceProof -> bdsMicroRaidenApi.makePayment(balanceProof, getSenderAddress(),
                // TODO: 06-06-2018 neuro actualizar balanço
                openBlockNumber, value, owedBalance, devAddress, storeAddress, oemAddress)
                .retryWhen(throwableObservable -> {
                  AtomicInteger counter = new AtomicInteger();
                  return throwableObservable.takeWhile(__ -> counter.getAndIncrement() != 5)
                      .flatMap(__ -> Observable.timer(5, TimeUnit.SECONDS));
                })
                .map(makePaymentResponse -> balanceProof.getBytes())
                .ignoreElements())
        .doOnError(throwable -> owedBalance = owedBalance.subtract(value))
        .blockingAwait();
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

  private byte[] createBalanceProof() {
    return microRaidenClient.createBalanceProof(senderECKey, receiverAddress, openBlockNumber,
        owedBalance);
  }

  private boolean hasFunds(BigInteger amount) {
    return (getBalance().subtract(amount)
        .compareTo(BigInteger.ZERO)) > 0;
  }
}
