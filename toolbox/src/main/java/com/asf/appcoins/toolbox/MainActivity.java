package com.asf.appcoins.toolbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.appcoins.sdk.android_appcoins_billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.android_appcoins_billing.CatapultBillingAppcoinsFactory;
import com.appcoins.sdk.android_appcoins_billing.FeatureType;
import com.appcoins.sdk.android_appcoins_billing.OnSkuDetailsResponseListener;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.SkuDetailsParam;

import java.util.ArrayList;
import java.util.List;

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

  CatapultAppcoinsBilling cab;

  public void onBuyGasButtonClicked(View arg0) {
    cab = CatapultBillingAppcoinsFactory.BuildAppcoinsBilling(this.getApplicationContext());
    cab.startService(result -> {
      Log.d("Message",result.getMessage());

    });
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
    SkuDetailsParam skuDetailsParam = new SkuDetailsParam();
    skuDetailsParam.setItemType(FeatureType.ITEM_TYPE_INAPP);
    ArrayList <String> al = new ArrayList<String>();
    al.add("test123");
    al.add("testreference");
    skuDetailsParam.setMoreItemSkus(al);
    Log.d("INICIAR SKU ASYNC","Iniciar sku async");

    cab.querySkuDetailsAsync(skuDetailsParam,new OnSkuDetailsResponseListener(){

      @Override
      public void onSkuDetailsResponseListener(int code, List<SkuDetails> skuDetailsList) {
        for(SkuDetails sd : skuDetailsList ) {
          Log.d("Title:",sd.getTitle());
          Log.d("Price:",sd.getPrice());
        }
      }
    });
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
