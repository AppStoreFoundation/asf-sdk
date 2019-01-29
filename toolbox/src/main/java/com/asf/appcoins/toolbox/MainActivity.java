package com.asf.appcoins.toolbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.appcoins.sdk.android_appcoins_billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.android_appcoins_billing.exception.IabException;
import com.appcoins.sdk.android_appcoins_billing.helpers.CatapultBillingAppCoinsFactory;
import com.appcoins.sdk.android_appcoins_billing.types.SkuType;
import com.appcoins.sdk.billing.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.Purchase;
import com.appcoins.sdk.billing.PurchasesResult;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends Activity {

  CatapultAppcoinsBilling cab;
  private CompositeDisposable compositeDisposable;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    compositeDisposable = new CompositeDisposable();
    cab = CatapultBillingAppCoinsFactory.BuildAppcoinsBilling(this.getApplicationContext(),
        BuildConfig.IAB_KEY);
    cab.startService(new AppCoinsBillingStateListener() {
      @Override public void onBillingSetupFinished(int responseCode) {
        Log.d("Message: ", responseCode + "");
        PurchasesResult pr = cab.queryPurchases(SkuType.INAPP);
        for (Purchase p : pr.getPurchases()) {
          Log.d("Purchase result token: ", p.getToken());
          Log.d("Purchase result sku: ", p.getSku());
        }
      }

      @Override public void onBillingServiceDisconnected() {
        Log.d("Message: ","Disconnected");
      }
    });
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
        Log.d("Message Key", key);
        Log.d("Message value", value.toString());
      }
    }
  }

  public void onBuyGasButtonClicked(View arg0) {
    startActivity(EmptyActivity.newIntent(this));
  }

  public void onUpgradeAppButtonClicked(View arg0) {
   /* cab.startService(result -> {
      Log.d("Message", result.getMessage());
    });
    PurchasesResult pr = cab.queryPurchases(SkuType.INAPP);
    Log.d("Purchase result", "-------------------------");
    Log.d("Purchase res resp code", pr.getResponseCode() + "");
    for (Purchase p : pr.getPurchases()) {
      Log.d("Purchase result token: ", p.getToken());
      Log.d("Purchase result sku: ", p.getSku());
    }
    Log.d("Purchase result", "-------------------------");    */
  }

  public void onCreateChannelButtonClicked(View view) throws IabException {
  }

  public void makePaymentButtonClicked(View view) {
   /* SkuDetailsParam skuDetailsParam = new SkuDetailsParam();
    skuDetailsParam.setItemType(SkuType.INAPP);
    ArrayList<String> al = new ArrayList<String>();

    al.add("gas");

    skuDetailsParam.setMoreItemSkus(al);
    Log.d("INICIAR SKU ASYNC", "Iniciar sku async");

    cab.querySkuDetailsAsync(skuDetailsParam, new OnSkuDetailsResponseListener() {

      @Override
      public void onSkuDetailsResponseListener(int code, List<SkuDetails> skuDetailsList) {
        ArrayList<String> al = new ArrayList<String>();

        for (SkuDetails sd : skuDetailsList) {
          al.add(sd.getSku());
          Log.d("Skus: ", sd.getSku());
        }

        if (skuDetailsList.size() > 0) {
          al = null;
          String payload = PayloadHelper.buildIntentPayload(Application.developerAddress, null);
          cab.launchPurchaseFlow(MainActivity.this, skuDetailsList.get(0)
                  .getSku(), SkuType.INAPP, al, 10001,
              (OnIabPurchaseFinishedListener) (result, info) -> {
                Log.d("aquiiiiiiiiiiiiiiii: ", ".....................");
                Log.d("result: ", result.getMessage());
                Log.d("Purchase: ", info.getSku());
              }, payload);
        }
      }
    });   */
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