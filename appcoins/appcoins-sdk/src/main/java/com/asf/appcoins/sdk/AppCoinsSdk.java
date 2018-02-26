package com.asf.appcoins.sdk;

import com.asf.appcoins.sdk.entity.Transaction;
import io.reactivex.Observable;

/**
 * Created by neuro on 26-02-2018.
 */

public interface AppCoinsSdk {

  Observable<Transaction> getTransaction(String txhash);

  void buy(String sku);
}
