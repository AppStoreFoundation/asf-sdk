package com.asf.appcoins.toolbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.asf.appcoins.sdk.iab.AppCoinsIab;
import com.asf.appcoins.sdk.iab.payment.PaymentDetails;
import com.asf.appcoins.sdk.iab.payment.PaymentStatus;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

  private final CompositeDisposable compositeDisposable;

  private AppCoinsIab appCoinsIab;

  public MainActivity() {
    this.compositeDisposable = new CompositeDisposable();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    appCoinsIab = AppCoinsIabSingleton.getAppCoinsIab();
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
}
