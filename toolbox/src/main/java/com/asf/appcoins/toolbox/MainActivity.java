package com.asf.appcoins.toolbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.asf.appcoins.sdk.AppCoinsSdk;
import com.asf.appcoins.sdk.payment.PaymentDetails;
import com.asf.appcoins.sdk.payment.PaymentStatus;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

  private final CompositeDisposable compositeDisposable;

  private AppCoinsSdk appCoinsSdk;

  public MainActivity() {
    this.compositeDisposable = new CompositeDisposable();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    appCoinsSdk = AppCoinsSdkSingleton.getAppCoinsSdk();
  }

  @Override protected void onDestroy() {
    compositeDisposable.dispose();

    super.onDestroy();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (appCoinsSdk.onActivityResult(requestCode, requestCode, data)) {
      compositeDisposable.add(appCoinsSdk.getCurrentPayment()
          .subscribe(paymentDetails -> runOnUiThread(() -> handlePayment(paymentDetails))));
    }
  }

  private void handlePayment(PaymentDetails paymentDetails) {
    if (paymentDetails.getPaymentStatus() == PaymentStatus.SUCCESS) {
      String skuId = paymentDetails.getSkuId();
      appCoinsSdk.consume(skuId);

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
    appCoinsSdk.buy(Skus.SKU_GAS_ID, this);
  }

  public void onUpgradeAppButtonClicked(View arg0) {
    appCoinsSdk.buy(Skus.SKU_PREMIUM_ID, this);
  }
}
