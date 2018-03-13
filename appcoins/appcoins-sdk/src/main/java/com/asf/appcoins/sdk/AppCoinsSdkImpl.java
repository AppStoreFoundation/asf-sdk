package com.asf.appcoins.sdk;

import android.app.Activity;
import android.content.Intent;
import com.asf.appcoins.sdk.entity.SKU;
import com.asf.appcoins.sdk.entity.Transaction.Status;
import com.asf.appcoins.sdk.payment.PaymentDetails;
import com.asf.appcoins.sdk.payment.PaymentService;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by neuro on 01-03-2018.
 */

final class AppCoinsSdkImpl implements AppCoinsSdk {

  private static final int DEFAULT_REQUEST_CODE = 3423;
  private static final int SUCCESS_RESULT_CODE = 0;

  private final int period;
  private final Scheduler scheduler;
  private final PaymentService paymentService;
  private final SkuManager skuManager;

  AppCoinsSdkImpl(int period, Scheduler scheduler, SkuManager skuManager,
      PaymentService paymentService, boolean debug) {
    this.period = period;
    this.scheduler = scheduler;
    this.skuManager = skuManager;
    this.paymentService = paymentService;
  }

  @Override public Observable<PaymentDetails> getPayment(String skuId) {
    return Observable.interval(period, TimeUnit.SECONDS, scheduler)
        .timeInterval()
        .switchMap(scan -> paymentService.getPaymentStatus(skuId))
        .takeUntil(paymentDetails -> paymentDetails.getTransaction()
            .getStatus() == Status.PENDING);
  }

  @Override public Observable<PaymentDetails> getLastPayment() {
    return Observable.fromCallable(paymentService::getLastPayment);
  }

  @Override public void consume(String skuId) {
    paymentService.consume(skuId);
  }

  @Override public void buy(String skuId, Activity activity) {
    paymentService.buy(skuId, activity, DEFAULT_REQUEST_CODE);
  }

  @Override public Collection<SKU> listSkus() {
    return skuManager.getSkus();
  }

  @Override public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    paymentService.onActivityResult(requestCode, resultCode, data);

    return (requestCode == DEFAULT_REQUEST_CODE) && ((data != null) && data.hasExtra(
        PaymentService.TRANSACTION_HASH_KEY));
  }
}
