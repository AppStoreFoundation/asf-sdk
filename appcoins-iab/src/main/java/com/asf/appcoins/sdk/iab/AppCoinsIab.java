package com.asf.appcoins.sdk.iab;

import android.app.Activity;
import android.content.Intent;
import com.asf.appcoins.sdk.iab.entity.SKU;
import com.asf.appcoins.sdk.iab.exception.ConsumeFailedException;
import com.asf.appcoins.sdk.iab.payment.PaymentDetails;
import io.reactivex.Completable;
import io.reactivex.Observable;
import java.util.Collection;

/**
 * Created by neuro on 26-02-2018.
 */

public interface AppCoinsIab {

  Observable<PaymentDetails> getPayment(String skuId);

  Observable<PaymentDetails> getCurrentPayment();

  void consume(String skuId) throws ConsumeFailedException;

  Completable buy(String skuId, Activity activity);

  Collection<SKU> listSkus();

  boolean onActivityResult(int requestCode, int resultCode, Intent data);
}
