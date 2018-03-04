package com.asf.appcoins.sdk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.asf.appcoins.sdk.entity.SKU;
import com.asf.appcoins.sdk.entity.Transaction;
import com.asf.appcoins.sdk.entity.Transaction.Status;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import java.math.BigDecimal;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by neuro on 01-03-2018.
 */

final class AppCoinsSdkImpl implements AppCoinsSdk {

  private static final int DEFAULT_PERIOD = 5;
  private static final int DECIMALS = 18;

  private static final int DEFAULT_REQUEST_CODE = 3423;
  private final int period;
  private final AsfWeb3j asfWeb3j;
  private final Scheduler scheduler;
  private final SkuManager skuManager;
  private final int networkId;
  private final String developerAddress;

  AppCoinsSdkImpl(AsfWeb3j asfWeb3j, String developerAddress, List<SKU> skuses) {
    this(asfWeb3j, DEFAULT_PERIOD, Schedulers.io(), developerAddress, new SkuManager(skuses),
        false);
  }

  AppCoinsSdkImpl(AsfWeb3j asfWeb3j, int period, Scheduler scheduler, String developerAddress,
      SkuManager skuManager, boolean debug) {
    this.asfWeb3j = asfWeb3j;
    this.period = period;
    this.scheduler = scheduler;
    this.developerAddress = developerAddress;
    this.skuManager = skuManager;
    this.networkId = debug ? 3 : 1;
  }

  @Override public Observable<Transaction> getTransaction(String txHash) {
    return Observable.interval(period, TimeUnit.SECONDS, scheduler)
        .timeInterval()
        .switchMap(scan -> asfWeb3j.getTransactionByHash(txHash))
        .takeUntil(transaction -> transaction.getStatus() == Status.PENDING);
  }

  @Override public void buy(String skuId, Activity activity) {
    Intent intent = new Intent(Intent.ACTION_VIEW);

    BigDecimal amount = skuManager.getSkuAmount(skuId);
    BigDecimal total = amount.multiply(BigDecimal.TEN.pow(DECIMALS));

    intent.setData(
        buildUri("0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3", networkId, developerAddress, total));
    activity.startActivityForResult(intent, DEFAULT_REQUEST_CODE);
  }

  Uri buildUri(String contractAddress, int networkId, String developerAddress, BigDecimal amount) {
    return buildUri(contractAddress, networkId, developerAddress, amount);
  }

  String buildUriString(String contractAddress, int networkId, String developerAddress,
      BigDecimal amount) {
    StringBuilder stringBuilder = new StringBuilder(4);
    Formatter formatter = new Formatter(stringBuilder);
    formatter.format("ethereum:%s@%d/transfer?address=%s&uint256=%s", contractAddress, networkId,
        developerAddress, amount.toString());

    return stringBuilder.toString();
  }
}
