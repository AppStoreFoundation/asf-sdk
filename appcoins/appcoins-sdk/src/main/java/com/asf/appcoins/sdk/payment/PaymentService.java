package com.asf.appcoins.sdk.payment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.asf.appcoins.sdk.AsfWeb3j;
import com.asf.appcoins.sdk.SkuManager;
import com.asf.appcoins.sdk.entity.Transaction;
import com.asf.appcoins.sdk.entity.Transaction.Status;
import com.asf.appcoins.sdk.util.UriBuilder;
import io.reactivex.Observable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neuro on 08-03-2018.
 */
public final class PaymentService {

  public static final String TRANSACTION_HASH_KEY = "transaction_hash";

  private static final int DECIMALS = 18;
  private final int networkId;
  private final SkuManager skuManager;
  private final String developerAddress;
  private final Map<String, PaymentDetails> payments;
  private final AsfWeb3j asfWeb3j;
  private PaymentDetails onGoingPayment;

  public PaymentService(int networkId, SkuManager skuManager, String developerAddress,
      AsfWeb3j asfWeb3j) {
    this.networkId = networkId;
    this.skuManager = skuManager;
    this.developerAddress = developerAddress;
    this.asfWeb3j = asfWeb3j;
    this.payments = new HashMap<>(1);
  }

  public void buy(String skuId, Activity activity, int defaultRequestCode) {
    Intent intent = new Intent(Intent.ACTION_VIEW);

    BigDecimal amount = skuManager.getSkuAmount(skuId);
    BigDecimal total = amount.multiply(BigDecimal.TEN.pow(DECIMALS));

    Uri data =
        UriBuilder.buildUri("0xab949343e6c369c6b17c7ae302c1debd4b7b61c3", skuId, networkId, total,
            developerAddress);
    intent.setData(data);

    if (payments.containsKey(skuId)) {
      throw new IllegalArgumentException(
          "Pending buy action with the same sku found! Did you forget to consume the former?");
    } else {
      onGoingPayment = new PaymentDetails(PaymentStatus.FAIL, skuId,
          new Transaction(null, null, developerAddress, total.toString(), Status.PENDING));
      payments.put(skuId, onGoingPayment);

      activity.startActivityForResult(intent, defaultRequestCode);
    }
  }

  public Observable<PaymentDetails> getPaymentDetails(String skuId) {
    return asfWeb3j.getTransactionByHash(getTransactionHash(skuId))
        .map(transaction -> {
          PaymentStatus paymentStatus =
              ((transaction.getStatus() == Status.ACCEPTED) ? PaymentStatus.SUCCESS
                  : PaymentStatus.FAIL);
          return new PaymentDetails(paymentStatus, skuId, transaction);
        });
  }

  private String getTransactionHash(String skuId) {
    return payments.get(skuId)
        .getTransaction()
        .getHash();
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    String skuId = onGoingPayment.getSkuId();
    if (data == null) {
    } else {
      String txHash = data.getStringExtra(TRANSACTION_HASH_KEY);

      if (txHash == null) {
        throw new IllegalStateException("Failed to get tx hash!");
      } else {
        onGoingPayment.getTransaction()
            .setHash(txHash);
      }

      if (onGoingPayment == null) {
        throw new IllegalStateException("Catastrofic Failure! No ongoing payment in course!");
      }

      onGoingPayment.setPaymentStatus(PaymentStatus.PENDING);
    }
  }

  public PaymentDetails getLastPayment() {
    return onGoingPayment;
  }

  public void consume(String skuId) {
    if (!payments.containsKey(skuId)) {
      throw new IllegalArgumentException(
          "Failed to consume " + skuId + '!' + System.lineSeparator() + "Did you buy it first?");
    }

    payments.remove(skuId);
  }
}
