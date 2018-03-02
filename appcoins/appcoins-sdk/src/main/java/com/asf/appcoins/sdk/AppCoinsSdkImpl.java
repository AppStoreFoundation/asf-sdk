package com.asf.appcoins.sdk;

import com.asf.appcoins.sdk.entity.Transaction;
import com.asf.appcoins.sdk.entity.Transaction.Status;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

/**
 * Created by neuro on 01-03-2018.
 */

class AppCoinsSdkImpl implements AppCoinsSdk {

  public static final int DEFAULT_PERIOD = 5;

  private final int period;
  private final AsfWeb3j asfWeb3j;
  private final Scheduler scheduler;

  AppCoinsSdkImpl(AsfWeb3j asfWeb3j) {
    this(asfWeb3j, DEFAULT_PERIOD, Schedulers.io());
  }

  AppCoinsSdkImpl(AsfWeb3j asfWeb3j, int period, Scheduler scheduler) {
    this.asfWeb3j = asfWeb3j;
    this.period = period;
    this.scheduler = scheduler;
  }

  @Override public Observable<Transaction> getTransaction(String txHash) {
    return Observable.interval(period, TimeUnit.SECONDS, scheduler)
        .timeInterval()
        .switchMap(scan -> asfWeb3j.getTransactionByHash(txHash))
        .takeUntil(transaction -> transaction.getStatus() == Status.PENDING);
  }

  @Override public void buy(String sku) {
    throw new RuntimeException("Not implemented yet");
  }
}
