package com.asf.appcoins.toolbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.asf.appcoins.sdk.iab.AppCoinsIab;
import com.asf.appcoins.sdk.iab.payment.PaymentDetails;
import com.asf.appcoins.sdk.iab.payment.PaymentStatus;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.MicroRaidenBDS;
import com.bds.microraidenj.channel.BDSChannel;
import ethereumj.crypto.ECKey;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

  private static final Address receiverAddress =
      Address.from("0x31a16aDF2D5FC73F149fBB779D20c036678b1bBD");

  private final CompositeDisposable compositeDisposable;

  private AppCoinsIab appCoinsIab;
  private MicroRaidenBDS microRaidenBDS;
  private ECKey senderECKey;
  private BDSChannel bdsChannel;

  public MainActivity() {
    this.compositeDisposable = new CompositeDisposable();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    appCoinsIab = AppCoinsIabSingleton.getAppCoinsIab();
    microRaidenBDS = BDSMicroRaidenjSingleton.getInstance();
    senderECKey = ECKey.fromPrivate(
        new BigInteger("24124c07ecc5e4d4bd01ef83d2d24295b31998fabebd9e7cb0c01b44589c137c", 16));
  }

  @Override protected void onDestroy() {
    compositeDisposable.dispose();

    super.onDestroy();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (appCoinsIab.onActivityResult(requestCode, requestCode, data)) {
      compositeDisposable.add(appCoinsIab.getCurrentPayment()
          .distinctUntilChanged(PaymentDetails::getPaymentStatus)
          .subscribe(paymentDetails -> runOnUiThread(() -> handlePayment(paymentDetails)),
              Throwable::printStackTrace));
    }
  }

  private void handlePayment(PaymentDetails paymentDetails) {
    if (paymentDetails.getPaymentStatus() == PaymentStatus.SUCCESS) {
      String skuId = paymentDetails.getSkuId();
      appCoinsIab.consume(skuId);

      if (Skus.SKU_GAS_ID.equals(skuId)) {
        Toast.makeText(this, Skus.SKU_GAS_LABEL, Toast.LENGTH_LONG)
            .show();
      } else {
        if (Skus.SKU_PREMIUM_ID.equals(skuId)) {
          Toast.makeText(this, Skus.SKU_PREMIUM_LABEL, Toast.LENGTH_LONG)
              .show();
        }
      }
    }
  }

  public void onBuyGasButtonClicked(View arg0) {
    Disposable disposable = appCoinsIab.buy(Skus.SKU_GAS_ID, this)
        .subscribe(() -> {
          Toast.makeText(this, "Buy successfully triggered.", Toast.LENGTH_SHORT)
              .show();
        }, throwable -> {
          Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT)
              .show();
        });
  }

  public void onUpgradeAppButtonClicked(View arg0) {
    appCoinsIab.buy(Skus.SKU_PREMIUM_ID, this);
  }

  public void onCreateChannelButtonClicked(View view) {
    compositeDisposable.add(
        microRaidenBDS.createChannel(senderECKey, receiverAddress, BigInteger.valueOf(10))
            .doOnSubscribe(disposable -> runOnUiThread(
                () -> Toast.makeText(this, "Creating channel", Toast.LENGTH_SHORT)
                    .show()))
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(bdsChannel -> Toast.makeText(this, "Channel Created.", Toast.LENGTH_SHORT)
                    .show())
            .subscribeOn(Schedulers.io())
            .subscribe(bdsChannel -> this.bdsChannel = bdsChannel));
  }

  public void makePaymentButtonClicked(View view) {
    if (!checkChannelAvailable()) {
      return;
    }

    Completable.defer(() -> bdsChannel.makePayment(BigInteger.ONE, receiverAddress, receiverAddress,
        receiverAddress)
        .toCompletable())
        .doOnSubscribe(disposable -> runOnUiThread(
            () -> Toast.makeText(this, "Making payment", Toast.LENGTH_SHORT)
                .show()))
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Toast.makeText(this, "Payment Failed!", Toast.LENGTH_SHORT)
            .show())
        .doOnComplete(() -> Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT)
            .show())
        .subscribeOn(Schedulers.io())
        .subscribe();
  }

  public void onCloseChannelButtonClicked(View view) {
    if (!checkChannelAvailable()) {
      return;
    } else {
      BDSChannel tmp = bdsChannel;
      bdsChannel = null;

      Single.fromCallable(() -> tmp.closeCooperatively(senderECKey))
          .observeOn(AndroidSchedulers.mainThread())
          .doOnSubscribe(disposable -> runOnUiThread(
              () -> Toast.makeText(this, "Closing channel", Toast.LENGTH_SHORT)
                  .show()))
          .doOnError(throwable -> Toast.makeText(this, "Transaction Failed!", Toast.LENGTH_SHORT)
              .show())
          .subscribeOn(Schedulers.io())
          .doOnSuccess(s -> Toast.makeText(this, "Channel Closed.", Toast.LENGTH_SHORT)
              .show())
          .subscribe();
    }
  }

  private boolean checkChannelAvailable() {
    if (bdsChannel == null) {
      Toast.makeText(this, "No channel available.", Toast.LENGTH_SHORT)
          .show();

      return false;
    }

    return true;
  }
}
