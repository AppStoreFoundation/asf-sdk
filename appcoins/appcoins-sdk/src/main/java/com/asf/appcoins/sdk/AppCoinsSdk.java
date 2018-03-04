package com.asf.appcoins.sdk;

import android.app.Activity;
import com.asf.appcoins.sdk.entity.SKU;
import com.asf.appcoins.sdk.entity.Transaction;
import io.reactivex.Observable;
import java.util.Collection;

/**
 * Created by neuro on 26-02-2018.
 */

public interface AppCoinsSdk {

  Observable<Transaction> getTransaction(String txhash);

  void buy(String sku, Activity activity);

  Collection<SKU> listSkus();
}
