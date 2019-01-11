package com.asf.appcoins.toolbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appcoins.sdk.android_appcoins_billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.android_appcoins_billing.CatapultBillingAppcoinsFactory;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

  private final CompositeDisposable compositeDisposable;

  public MainActivity() {
    this.compositeDisposable = new CompositeDisposable();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override protected void onDestroy() {
    compositeDisposable.dispose();

    super.onDestroy();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }

  public void onBuyGasButtonClicked(View arg0) {
    Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT)
        .show();
    CatapultAppcoinsBilling cab = CatapultBillingAppcoinsFactory.BuildAppcoinsBilling(this);
    cab.startService(result -> Log.d("Resultado",result.getMessage()));
  }

  public void onUpgradeAppButtonClicked(View arg0) {
    Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT)
        .show();
  }

  public void onCreateChannelButtonClicked(View view) {
    Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT)
        .show();
  }

  public void makePaymentButtonClicked(View view) {

  }

  public void onCloseChannelButtonClicked(View view) {
    Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT)
        .show();
  }

  private boolean checkChannelAvailable() {
    Toast.makeText(this, "No channel available.", Toast.LENGTH_SHORT)
        .show();

    return false;
  }
}
