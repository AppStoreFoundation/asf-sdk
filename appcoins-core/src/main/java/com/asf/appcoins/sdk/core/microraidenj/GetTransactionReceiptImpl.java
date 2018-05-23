package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.appcoins.sdk.core.microraidenj.mapper.TransactionMapper;
import com.asf.microraidenj.entities.TransactionReceipt;
import com.asf.microraidenj.eth.interfaces.GetTransactionReceipt;
import com.asf.microraidenj.exception.TransactionNotFoundException;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.concurrent.TimeUnit;
import org.web3j.protocol.Web3j;

public class GetTransactionReceiptImpl implements GetTransactionReceipt {

  private final Web3j web3j;
  private final int timeout;
  private final int period;

  public GetTransactionReceiptImpl(Web3j web3j, int period, int timeout) {
    this.web3j = web3j;
    this.period = period;
    this.timeout = timeout;
  }

  @Override public Single<TransactionReceipt> get(String txHash) {
    org.web3j.protocol.core.methods.response.TransactionReceipt transactionReceipt;
    return Observable.fromCallable(() -> web3j.ethGetTransactionReceipt(txHash)
        .send()
        .getTransactionReceipt())
        .retryWhen(flowable -> flowable.zipWith(Observable.interval(period, TimeUnit.SECONDS),
            (throwable, integer) -> integer))
        .singleOrError()
        .timeout(timeout, TimeUnit.SECONDS, Single.error(new TransactionNotFoundException(txHash)))
        .map(TransactionMapper::from);
  }
}
