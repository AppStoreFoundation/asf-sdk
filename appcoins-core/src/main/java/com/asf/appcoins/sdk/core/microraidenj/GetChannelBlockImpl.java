package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.microraidenj.eth.interfaces.GetChannelBlock;
import com.asf.microraidenj.exception.TransactionNotFoundException;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import org.web3j.protocol.Web3j;

public class GetChannelBlockImpl implements GetChannelBlock {

  private final Web3j web3j;
  private final int timeout;
  private final int period;

  public GetChannelBlockImpl(Web3j web3j, int period, int timeout) {
    this.web3j = web3j;
    this.period = period;
    this.timeout = timeout;
  }

  @Override public BigInteger get(String createChannelTxHash) {
    return Observable.fromCallable(() -> web3j.ethGetTransactionReceipt(createChannelTxHash)
        .send()
        .getTransactionReceipt())
        .retryWhen(flowable -> flowable.zipWith(Observable.interval(period, TimeUnit.SECONDS),
            (throwable, integer) -> integer))
        .singleOrError()
        .timeout(timeout, TimeUnit.SECONDS,
            Single.error(new TransactionNotFoundException(createChannelTxHash)))
        .map(org.web3j.protocol.core.methods.response.TransactionReceipt::getBlockNumber)
        .blockingGet();
  }
}
