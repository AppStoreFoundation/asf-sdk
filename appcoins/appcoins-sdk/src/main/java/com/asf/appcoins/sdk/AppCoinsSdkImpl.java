package com.asf.appcoins.sdk;

import com.asf.appcoins.sdk.entity.Transaction;
import io.reactivex.Observable;

/**
 * Created by neuro on 01-03-2018.
 */

class AppCoinsSdkImpl implements AppCoinsSdk {
  private final AsfWeb3j asfWeb3j;

  AppCoinsSdkImpl(AsfWeb3j asfWeb3j) {
    this.asfWeb3j = asfWeb3j;
  }

  @Override public Observable<Transaction> getTransaction(String txhash) {
    return asfWeb3j.getTransactionByHash(txhash);
  }

  @Override public void buy(String sku) {
    throw new RuntimeException("Not implemented yet");
  }
}
