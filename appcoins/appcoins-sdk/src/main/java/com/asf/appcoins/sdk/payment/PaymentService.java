package com.asf.appcoins.sdk.payment;

import android.R.drawable;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.asf.appcoins.sdk.AsfWeb3j;
import com.asf.appcoins.sdk.SkuManager;
import com.asf.appcoins.sdk.entity.Transaction;
import com.asf.appcoins.sdk.entity.Transaction.Status;
import com.asf.appcoins.sdk.util.UriBuilder;
import com.asf.appcoins.sdk.wallet.AndroidUtils;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
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
    BigDecimal amount = skuManager.getSkuAmount(skuId);
    BigDecimal total = amount.multiply(BigDecimal.TEN.pow(DECIMALS));

    Intent intent = buildPaymentIntent(skuId, total);

    if (AndroidUtils.hasHandlerAvailable(intent, activity)) {
      if (payments.containsKey(skuId)) {
        throw new IllegalArgumentException(
            "Pending buy action with the same sku found! Did you forget to consume the former?");
      } else {
        onGoingPayment = new PaymentDetails(PaymentStatus.FAIL, skuId,
            new Transaction(null, null, developerAddress, total.toString(), Status.PENDING));
        payments.put(skuId, onGoingPayment);

        activity.startActivityForResult(intent, defaultRequestCode);
      }
    } else {
      showWalletInstallDialog(activity).filter(aBoolean -> aBoolean)
          .doOnSuccess(gotoStore(activity))
          .subscribe(aBoolean -> {
          }, Throwable::printStackTrace);
    }
  }

  @NonNull private Consumer<Boolean> gotoStore(Activity activity) {
    return aBoolean -> {
      String appPackageName = "com.asf.wallet";
      try {
        activity.startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
      } catch (android.content.ActivityNotFoundException anfe) {
        activity.startActivity(new Intent(Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
      }
    };
  }

  private Single<Boolean> showWalletInstallDialog(Context context) {
    return Single.create(emitter -> {
      Builder builder;
      builder = new Builder(context);
      builder.setTitle("Appc Wallet Missing")
          .setMessage("Do you want to install the ASF wallet?")
          .setPositiveButton(string.yes, (dialog, which) -> emitter.onSuccess(true))
          .setNegativeButton(string.no, (dialog, which) -> emitter.onSuccess(false))
          .setIcon(drawable.ic_dialog_alert)
          .show();
    });
  }

  @NonNull private Intent buildPaymentIntent(String skuId, BigDecimal total) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    Uri data =
        UriBuilder.buildUri("0xab949343e6c369c6b17c7ae302c1debd4b7b61c3", skuId, networkId, total,
            developerAddress);
    intent.setData(data);
    return intent;
  }

  public Observable<PaymentDetails> getPaymentDetails(String skuId) {
    return asfWeb3j.getTransactionByHash(getTransactionHash(skuId))
        .map(transaction -> new PaymentDetails(PaymentStatus.from(transaction.getStatus()), skuId,
            transaction));
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
