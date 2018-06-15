package com.asf.appcoins.sdk.iab.payment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.asf.appcoins.sdk.R;
import com.asf.appcoins.sdk.core.transaction.Transaction;
import com.asf.appcoins.sdk.core.transaction.Transaction.Status;
import com.asf.appcoins.sdk.core.util.wallet.WalletUtils;
import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import com.asf.appcoins.sdk.iab.SkuManager;
import com.asf.appcoins.sdk.iab.entity.SKU;
import com.asf.appcoins.sdk.iab.exception.ConsumeFailedException;
import com.asf.appcoins.sdk.iab.util.UriBuilder;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neuro on 08-03-2018.
 */
public final class PaymentService {

  public static final String TRANSACTION_HASH_KEY = "transaction_hash";
  public static final String PRODUCT_NAME = "product_name";

  private static final String WALLET_PACKAGE_NAME = "com.asfoundation.wallet";

  private static final int DECIMALS = 18;
  private final int networkId;
  private final SkuManager skuManager;
  private final String developerAddress;
  private final Map<String, PaymentDetails> payments;
  private final AsfWeb3j asfWeb3j;
  private final String tokenContractAddress;
  private final String iabContractAddress;

  private PaymentDetails currentPayment;

  public PaymentService(int networkId, SkuManager skuManager, String developerAddress,
      AsfWeb3j asfWeb3j, String tokenContractAddress, String iabContractAddress) {
    this.networkId = networkId;
    this.skuManager = skuManager;
    this.developerAddress = developerAddress;
    this.asfWeb3j = asfWeb3j;
    this.iabContractAddress = iabContractAddress;
    this.payments = new HashMap<>(1);
    this.tokenContractAddress = tokenContractAddress;
  }

  public Single<Boolean> buy(String skuId, Activity activity, int defaultRequestCode) {
    SKU sku = skuManager.getSku(skuId);
    BigDecimal amount = skuManager.getSkuAmount(skuId);
    BigDecimal total = amount.multiply(BigDecimal.TEN.pow(DECIMALS));

    Intent intent = buildPaymentIntent(sku, total, tokenContractAddress, iabContractAddress);

    currentPayment = new PaymentDetails(PaymentStatus.FAIL, skuId,
        new Transaction(null, null, developerAddress, total.toString(), Status.PENDING));

    if (WalletUtils.hasWalletInstalled(activity)) {
      if (payments.containsKey(skuId)) {
        throw new IllegalArgumentException(
            "Pending buy action with the same sku found! Did you forget to consume the former?");
      } else {
        payments.put(skuId, currentPayment);

        activity.startActivityForResult(intent, defaultRequestCode);
      }
      return Single.just(true);
    } else {
      return WalletUtils.promptToInstallWallet(activity,
          activity.getString(R.string.install_wallet_from_iab));
    }
  }

  @NonNull
  private Intent buildPaymentIntent(SKU sku, BigDecimal amount, String tokenContractAddress,
      String iabContractAddress) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    Uri data = Uri.parse(UriBuilder.buildUriString(tokenContractAddress, iabContractAddress, amount,
        developerAddress, sku.getId(), networkId));
    intent.setData(data);

    intent.putExtra(PRODUCT_NAME, sku.getName());

    return intent;
  }

  public Observable<PaymentDetails> getPaymentDetails(String skuId) {
    return getPaymentDetails(skuId, getTransactionHash(skuId));
  }

  public Observable<PaymentDetails> getPaymentDetails(String skuId, String transactionHash) {
    if (payments.get(skuId) != null) {
      return asfWeb3j.getTransactionByHash(transactionHash)
          .subscribeOn(Schedulers.io())
          .map(transaction -> new PaymentDetails(PaymentStatus.from(transaction.getStatus()), skuId,
              transaction));
    } else {
      throw new IllegalArgumentException("SkuId not present! " + skuId);
    }
  }

  public Observable<PaymentDetails> getPaymentDetailsUnchecked(String skuId,
      String transactionHash) {
    return asfWeb3j.getTransactionByHash(transactionHash)
        .subscribeOn(Schedulers.io())
        .map(this::mapPendingToSuccess)
        .map(transaction -> new PaymentDetails(PaymentStatus.from(transaction.getStatus()), skuId,
            transaction));
  }

  private Transaction mapPendingToSuccess(Transaction transaction) {
    if (transaction.getStatus() == Status.PENDING) {
      String hash = transaction.getHash();
      String from = transaction.getFrom();
      String to = transaction.getTo();
      String value = transaction.getValue();
      Status status = Status.ACCEPTED;

      return new Transaction(hash, from, to, value, status);
    } else {
      return transaction;
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

      if (isMicroRaidenTransaction(txHash)) {
        currentPayment.setPaymentStatus(PaymentStatus.PENDING);
      } else {
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
  }

  private boolean isMicroRaidenTransaction(String txHash) {
    return txHash.length() > 66;
  }

  public PaymentDetails getCurrentPayment() {
    return currentPayment;
  }

  public void consume(String skuId) throws ConsumeFailedException {
    if (!payments.containsKey(skuId)) {
      throw new ConsumeFailedException(
          "Failed to consume " + skuId + '!' + System.lineSeparator() + "Did you buy it first?");
    }

    payments.remove(skuId);
  }
}
