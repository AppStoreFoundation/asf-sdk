package com.asf.appcoins.sdk.iab;

import android.app.Activity;
import android.content.Intent;
import com.asf.appcoins.sdk.core.transaction.Transaction.Status;
import com.asf.appcoins.sdk.iab.entity.SKU;
import com.asf.appcoins.sdk.iab.exception.ConsumeFailedException;
import com.asf.appcoins.sdk.iab.payment.PaymentDetails;
import com.asf.appcoins.sdk.iab.payment.PaymentService;
import com.asf.appcoins.sdk.iab.payment.PaymentStatus;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by neuro on 01-03-2018.
 */

final class AppCoinsIabImpl implements AppCoinsIab {

  private static final int DEFAULT_REQUEST_CODE = 3423;
  private static final int SUCCESS_RESULT_CODE = 0;

  private final int period;
  private final Scheduler scheduler;
  private final PaymentService paymentService;
  private final SkuManager skuManager;

  AppCoinsIabImpl(int period, Scheduler scheduler, SkuManager skuManager,
      PaymentService paymentService) {
    this.period = period;
    this.scheduler = scheduler;
    this.skuManager = skuManager;
    this.paymentService = paymentService;
  }

  private Observable<PaymentDetails> getPayment(String skuId) {
    return Observable.interval(0, period, TimeUnit.SECONDS, scheduler)
        .timeInterval()
        .switchMap(scan -> paymentService.getPaymentDetails(skuId))
        .takeUntil(paymentDetails -> paymentDetails.getTransaction()
            .getStatus() == Status.ACCEPTED);
  }

  @Override public Observable<PaymentDetails> getCurrentPayment() {
    PaymentDetails currentPayment = paymentService.getCurrentPayment();
    String txHash = currentPayment.getTransaction()
        .getHash();
    String skuId = currentPayment.getSkuId();

    boolean hasTxHash = txHash != null;

    if (hasTxHash) {
      return Observable.interval(0, period, TimeUnit.SECONDS, scheduler)
          .flatMap(longTimed -> paymentService.getPaymentDetailsUnchecked(skuId, txHash))
          .takeUntil(paymentDetails -> paymentDetails.getPaymentStatus() == PaymentStatus.SUCCESS);
    } else {
      return Observable.just(currentPayment);
    }
  }

  @Override public void consume(String skuId) throws ConsumeFailedException {
    paymentService.consume(skuId);
  }

  @Override public Completable buy(String skuId, Activity activity) {
    return paymentService.buy(skuId, activity, DEFAULT_REQUEST_CODE)
        .flatMap(aBoolean -> {
          if (aBoolean) {
            return Single.just(true);
          } else {
            return Single.error(new IllegalStateException("User didn't install wallet!"));
          }
        })
        .toCompletable();
  }

  @Override public Collection<SKU> listSkus() {
    return skuManager.getSkus();
  }

  @Override public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    paymentService.onActivityResult(requestCode, resultCode, data);

    return (requestCode == DEFAULT_REQUEST_CODE);
  }
}
