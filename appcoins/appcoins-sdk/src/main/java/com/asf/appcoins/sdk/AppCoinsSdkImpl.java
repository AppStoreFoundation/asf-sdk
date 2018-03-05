package com.asf.appcoins.sdk;

import android.app.Activity;
import android.content.Intent;
import com.asf.appcoins.sdk.entity.PurchaseResult;
import com.asf.appcoins.sdk.entity.PurchaseResult.Status;
import com.asf.appcoins.sdk.entity.SKU;
import com.asf.appcoins.sdk.entity.Transaction;
import com.asf.appcoins.sdk.util.UriBuilder;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by neuro on 01-03-2018.
 */

final class AppCoinsSdkImpl implements AppCoinsSdk {

  private static final int DECIMALS = 18;

  private static final int DEFAULT_REQUEST_CODE = 3423;
  private static final int SUCCESS_RESULT_CODE = 0;
  private static final String TRANSACTION_HASH = "transaction_hash";

  private final int period;
  private final AsfWeb3j asfWeb3j;
  private final Scheduler scheduler;
  private final SkuManager skuManager;
  private final int networkId;
  private final String developerAddress;
  private final String oemAddress;
  private final String storeAddress;

  AppCoinsSdkImpl(AsfWeb3j asfWeb3j, int period, Scheduler scheduler, String developerAddress,
      String oemAddress, String storeAddress, SkuManager skuManager, boolean debug) {
    this.asfWeb3j = asfWeb3j;
    this.period = period;
    this.scheduler = scheduler;
    this.developerAddress = developerAddress;
    this.oemAddress = oemAddress;
    this.storeAddress = storeAddress;
    this.skuManager = skuManager;
    this.networkId = debug ? 3 : 1;
  }

  @Override public Observable<Transaction> getTransaction(String txHash) {
    return Observable.interval(period, TimeUnit.SECONDS, scheduler)
        .timeInterval()
        .switchMap(scan -> asfWeb3j.getTransactionByHash(txHash))
        .takeUntil(transaction -> transaction.getStatus() == Transaction.Status.PENDING);
  }

  @Override public void buy(String skuId, Activity activity) {
    Intent intent = new Intent(Intent.ACTION_VIEW);

    BigDecimal amount = skuManager.getSkuAmount(skuId);
    BigDecimal total = amount.multiply(BigDecimal.TEN.pow(DECIMALS));

    intent.setData(
        UriBuilder.buildUri("0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3", networkId, total,
            developerAddress, oemAddress, storeAddress));
    activity.startActivityForResult(intent, DEFAULT_REQUEST_CODE);
  }

  @Override public Collection<SKU> listSkus() {
    return skuManager.getSkus();
  }

  @Override public PurchaseResult onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == DEFAULT_REQUEST_CODE) {
      if (resultCode == SUCCESS_RESULT_CODE) {
        return new PurchaseResult(Status.SUCCESS, data.getStringExtra(TRANSACTION_HASH),
            SUCCESS_RESULT_CODE);
      } else {
        return new PurchaseResult(Status.FAIL, resultCode);
      }
    }

    return new PurchaseResult(Status.FAIL, null, resultCode);
  }
}
