package com.asf.appcoins.sdk;

import android.app.Activity;
import android.content.Intent;
import com.asf.appcoins.sdk.entity.PurchaseResult;
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

  PurchaseResult onActivityResult(int requestCode, int resultCode, Intent data);
}
