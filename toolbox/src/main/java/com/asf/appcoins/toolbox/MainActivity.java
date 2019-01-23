package com.asf.appcoins.toolbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appcoins.sdk.android_appcoins_billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.android_appcoins_billing.CatapultBillingAppcoinsFactory;

import com.appcoins.sdk.android_appcoins_billing.IabException;

import com.appcoins.sdk.android_appcoins_billing.SkuType;

import com.appcoins.sdk.billing.Purchase;
import com.appcoins.sdk.billing.PurchasesResult;

import com.appcoins.sdk.android_appcoins_billing.FeatureType;

import com.appcoins.sdk.android_appcoins_billing.OnIabPurchaseFinishedListener;
import com.appcoins.sdk.android_appcoins_billing.OnSkuDetailsResponseListener;
import com.appcoins.sdk.android_appcoins_billing.PayloadHelper;

import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.SkuDetailsParam;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends Activity {

    
  
  private final CompositeDisposable compositeDisposable;

  CatapultAppcoinsBilling cab;

  
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
    Log.d("Activity Result: ", "onActivityResult(" + requestCode + "," + resultCode + "," + data);
    Bundle bundle = data.getExtras();
    if (bundle != null) {
      for (String key : bundle.keySet()) {
        Object value = bundle.get(key);
        Log.d("Message Key",key);
        Log.d("Message value" ,value.toString());
      }
    }

  }

    public void onBuyGasButtonClicked(View arg0) {
        String base64EncodedPublicKey = BuildConfig.IAB_KEY;
        cab = CatapultBillingAppcoinsFactory.BuildAppcoinsBilling(this.getApplicationContext(), base64EncodedPublicKey);
        cab.startService(result -> {
            Log.d("Message",result.getMessage());
        });
    }

    public void onUpgradeAppButtonClicked(View arg0) {
        String base64EncodedPublicKey = BuildConfig.IAB_KEY;
        PurchasesResult pr = cab.queryPurchases(SkuType.INAPP);
        Log.d("Purchase result", "-------------------------");
        Log.d("Purchase res resp code", pr.getResponseCode() + "");
        for(Purchase p : pr.getPurchases()){
            Log.d("Purchase result token: ", p.getToken());
            Log.d("Purchase result sku: ", p.getSku());
        }
        Log.d("Purchase result", "-------------------------");
    }

    public void onCreateChannelButtonClicked(View view) throws IabException {
    }

    public void makePaymentButtonClicked(View view) {
    SkuDetailsParam skuDetailsParam = new SkuDetailsParam();
    skuDetailsParam.setItemType(FeatureType.ITEM_TYPE_INAPP);
    ArrayList <String> al = new ArrayList<String>();

    al.add("gas");


    skuDetailsParam.setMoreItemSkus(al);
    Log.d("INICIAR SKU ASYNC","Iniciar sku async");

    cab.querySkuDetailsAsync(skuDetailsParam,new OnSkuDetailsResponseListener(){

      @Override
      public void onSkuDetailsResponseListener(int code, List<SkuDetails> skuDetailsList) {
        ArrayList<String> al = new ArrayList<String>();

        for(SkuDetails sd : skuDetailsList ) {
          al.add(sd.getSku());
          Log.d("Skus: ",sd.getSku());
        }

        if(skuDetailsList.size() > 0 ) {
          al = null;
          String payload =
                  PayloadHelper.buildIntentPayload(Application.developerAddress,
                          null);
          cab.launchPurchaseFlow(MainActivity.this, skuDetailsList.get(0).getSku(), FeatureType.ITEM_TYPE_INAPP, al, 10001, (OnIabPurchaseFinishedListener) (result, info) -> {
            Log.d("aquiiiiiiiiiiiiiiii: ",".....................");
            Log.d("result: ",result.getMessage());
            Log.d("Purchase: ",info.getSku());
          },payload);
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