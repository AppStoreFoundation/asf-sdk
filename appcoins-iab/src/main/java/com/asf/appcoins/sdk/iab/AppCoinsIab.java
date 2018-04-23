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
 * AppCoins IAB SDK interface.
 */
public interface AppCoinsIab {

  /**
   * Get the current payment (ie. last).
   *
   * @return an {@link Observable} that will emit {@link PaymentDetails} on each state change until
   * the transaction is accepted.
   */
  Observable<PaymentDetails> getCurrentPayment();

  /**
   * Tell the sdk to consume a previously bought sku.
   *
   * @param skuId skuId previously bought.
   *
   * @throws ConsumeFailedException in case there's no available sku to consume with the given id.
   */
  void consume(String skuId) throws ConsumeFailedException;

  /**
   * Starts the buy process. <br>
   * In case there's no compatible wallet installed, the sdk will prompt the user to install the
   * ASF Wallet.
   *
   * @param skuId the id of one of the previously registered skus.
   * @param activity activity that will be used to call the wallet.
   *
   * @return a completable that completes in case there's a compatible wallet installed and the buy
   * process was successfully initiated (ie. the wallet was called and waiting for user
   * confirmation).
   */
  Completable buy(String skuId, Activity activity);

  /**
   * @return a list of the available skus.
   */
  Collection<SKU> listSkus();

  /**
   * This method needs to be called on {@link Activity#onActivityResult(int, int, Intent)} in order
   * for the sdk to be able to handle the result of the purchase.
   *
   * @return true if this call was resultant of an sdk invocation.
   */
  boolean onActivityResult(int requestCode, int resultCode, Intent data);
}
