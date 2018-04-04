package com.asf.appcoins.sdk.iab.payment;

import android.R.drawable;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.asf.appcoins.sdk.iab.AsfWeb3j;
import com.asf.appcoins.sdk.iab.SkuManager;
import com.asf.appcoins.sdk.iab.entity.SKU;
import com.asf.appcoins.sdk.iab.entity.Transaction;
import com.asf.appcoins.sdk.iab.entity.Transaction.Status;
import com.asf.appcoins.sdk.iab.util.UriBuilder;
import com.asf.appcoins.sdk.iab.wallet.AndroidUtils;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neuro on 08-03-2018.
 */
public final class PaymentService {

  public static final String TRANSACTION_HASH_KEY = "transaction_hash";
  public static final String PRODUCT_NAME = "product_name";

  private static final int DECIMALS = 18;
  private final int networkId;
  private final SkuManager skuManager;
  private final String developerAddress;
  private final Map<String, PaymentDetails> payments;
  private final AsfWeb3j asfWeb3j;

  private PaymentDetails currentPayment;

  public PaymentService(int networkId, SkuManager skuManager, String developerAddress,
      AsfWeb3j asfWeb3j) {
    this.networkId = networkId;
    this.skuManager = skuManager;
    this.developerAddress = developerAddress;
    this.asfWeb3j = asfWeb3j;
    this.payments = new HashMap<>(1);
  }

  public void buy(String skuId, Activity activity, int defaultRequestCode) {
    SKU sku = skuManager.getSku(skuId);
    BigDecimal amount = skuManager.getSkuAmount(skuId);
    BigDecimal total = amount.multiply(BigDecimal.TEN.pow(DECIMALS));

    Intent intent = buildPaymentIntent(sku, total);

    currentPayment = new PaymentDetails(PaymentStatus.FAIL, skuId,
        new Transaction(null, null, developerAddress, total.toString(), Status.PENDING));

    if (AndroidUtils.hasHandlerAvailable(intent, activity)) {
      if (payments.containsKey(skuId)) {
        throw new IllegalArgumentException(
            "Pending buy action with the same sku found! Did you forget to consume the former?");
      } else {
        payments.put(skuId, currentPayment);

        activity.startActivityForResult(intent, defaultRequestCode);
      }
    } else {
      Disposable subscribe = showWalletInstallDialog(activity).filter(aBoolean -> aBoolean)
          .doOnSuccess(gotoStore(activity))
          .subscribe(aBoolean -> {
          }, Throwable::printStackTrace);
    }
  }

  @NonNull private Consumer<Boolean> gotoStore(Activity activity) {
    return aBoolean -> {
      String appPackageName = "com.asfoundation.wallet";
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
      builder.setTitle("APPC Wallet Missing")
          .setMessage("To complete your purchase, you have to install an AppCoins wallet")
          .setPositiveButton(string.yes, (dialog, which) -> emitter.onSuccess(true))
          .setNegativeButton(string.no, (dialog, which) -> emitter.onSuccess(false))
          .setIcon(drawable.ic_dialog_alert)
          .show();
    });
  }

  @NonNull private Intent buildPaymentIntent(SKU sku, BigDecimal total) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    Uri data =
        UriBuilder.buildUri("0xab949343e6c369c6b17c7ae302c1debd4b7b61c3", sku.getId(), networkId,
            total, developerAddress);
    intent.setData(data);

    intent.putExtra(PRODUCT_NAME, sku.getName());

    return intent;
  }

  public Observable<PaymentDetails> getPaymentDetails(String skuId) {
    if (payments.get(skuId) != null) {
      return asfWeb3j.getTransactionByHash(getTransactionHash(skuId))
          .map(transaction -> new PaymentDetails(PaymentStatus.from(transaction.getStatus()), skuId,
              transaction));
    } else {
      throw new IllegalArgumentException("SkuId not present! " + skuId);
    }
  }

  private String getTransactionHash(String skuId) {
    return payments.get(skuId)
        .getTransaction()
        .getHash();
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    String skuId = currentPayment.getSkuId();
    if ((data == null) || !data.hasExtra(TRANSACTION_HASH_KEY)) {
      payments.remove(skuId);
    } else {
      String txHash = data.getStringExtra(TRANSACTION_HASH_KEY);

      if (txHash == null) {
        throw new IllegalStateException("Failed to get tx hash!");
      } else {
        currentPayment.getTransaction()
            .setHash(txHash);
      }

      if (currentPayment == null) {
        throw new IllegalStateException("Catastrofic Failure! No ongoing payment in course!");
      }

      currentPayment.setPaymentStatus(PaymentStatus.PENDING);
    }
  }

  public PaymentDetails getCurrentPayment() {
    return currentPayment;
  }

  public void consume(String skuId) {
    if (!payments.containsKey(skuId)) {
      throw new IllegalArgumentException(
          "Failed to consume " + skuId + '!' + System.lineSeparator() + "Did you buy it first?");
    }

    payments.remove(skuId);
  }
}
