package com.bds.microraidenj;

import com.asf.microraidenj.eth.ChannelBlockObtainer;
import com.asf.microraidenj.exception.TransactionNotFoundException;
import com.asf.microraidenj.type.ByteArray;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import org.web3j.protocol.Web3j;

public class DefaultChannelBlockObtainer implements ChannelBlockObtainer {

  private final Web3j web3j;
  private final int timeout;
  private final int period;

  public DefaultChannelBlockObtainer(Web3j web3j, int period, int timeout) {
    this.web3j = web3j;
    this.period = period;
    this.timeout = timeout;
  }

  @Override public BigInteger get(ByteArray createChannelTxHash) {
    return Observable.fromCallable(
        () -> web3j.ethGetTransactionReceipt(createChannelTxHash.toHexString(true))
        .send()
        .getTransactionReceipt())
        .retryWhen(flowable -> flowable.zipWith(Observable.interval(period, TimeUnit.SECONDS),
            (throwable, integer) -> integer))
        .singleOrError()
        .timeout(timeout, TimeUnit.SECONDS,
            Single.error(new TransactionNotFoundException(createChannelTxHash.toHexString())))
        .map(org.web3j.protocol.core.methods.response.TransactionReceipt::getBlockNumber)
        .blockingGet();
  }
}
