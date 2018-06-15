package com.bds.microraidenj.channel;

import com.asf.microraidenj.MicroRaidenClient;
import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import com.asf.microraidenj.type.ByteArray;
import com.bds.microraidenj.ws.BDSMicroRaidenApi;
import com.bds.microraidenj.ws.ChannelHistoryResponse;
import com.bds.microraidenj.ws.CloseChannelResponse;
import com.bds.microraidenj.ws.MakePaymentResponse;
import com.bds.microraidenj.ws.Type;
import ethereumj.crypto.ECKey;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.spongycastle.util.encoders.Hex;
import retrofit2.HttpException;

public final class BDSChannelImpl implements BDSChannel {

  private final ECKey senderECKey;
  private final Address receiverAddress;
  private final BigInteger openBlockNumber;
  private final MicroRaidenClient microRaidenClient;
  private final BDSMicroRaidenApi bdsMicroRaidenApi;

  private BigInteger owedBalance;
  private BigInteger totalBalance;

  public BDSChannelImpl(ECKey senderECKey, Address receiverAddress,
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

  public BDSChannelImpl(ECKey senderECKey, Address receiverAddress,
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

  @Override
  public Single<String> makePayment(BigInteger value, Address devAddress, Address storeAddress,
      Address oemAddress) {
    return Single.fromCallable(() -> {
      if (hasFunds(value)) {
        return createBalanceProof();
      } else {
        throw new InsufficientFundsException(
            "Insuficient funds! Available: " + getBalance().add(value) + ", Requested: " + value);
      }
    })
        .doOnSubscribe(disposable -> owedBalance = owedBalance.add(value))
        .map(ByteArray::new)
        .flatMap(
            balanceProof -> bdsMicroRaidenApi.makePayment(balanceProof, getSenderAddress(),
                // TODO: 06-06-2018 neuro actualizar balanço
                openBlockNumber, value, owedBalance, devAddress, storeAddress, oemAddress)
                .retryWhen(this::handleWsError)
                .map(MakePaymentResponse::getResult)
                .singleOrError())
        .doOnError(throwable -> owedBalance = owedBalance.subtract(value));
  }

  @Override public Single<List<ChannelHistoryResponse.MicroTransaction>> listTransactions() {
    return bdsMicroRaidenApi.channelHistory(getSenderAddress(), receiverAddress, openBlockNumber,
        Type.Payment)
        .singleOrError()
        .map(ChannelHistoryResponse::getResult);
  }

  private ObservableSource<?> handleWsError(Observable<Throwable> throwableObservable) {
    AtomicInteger counter = new AtomicInteger();

    return throwableObservable.flatMap(throwable -> {
      if (throwable instanceof HttpException) {
        return Observable.just(throwable)
            .takeWhile(__ -> counter.getAndIncrement() != 5)
            .flatMap(__ -> Observable.timer(5, TimeUnit.SECONDS));
      } else {
        return Observable.just(throwable);
      }
    });
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
    return (getBalance().compareTo(BigInteger.ZERO)) >= 0;
  }
}
