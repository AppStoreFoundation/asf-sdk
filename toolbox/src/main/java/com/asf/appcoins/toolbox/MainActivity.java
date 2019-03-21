package com.asf.appcoins.toolbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.appcoins.sdk.android_appcoins_billing.AppcoinsBillingClient;
import com.appcoins.sdk.android_appcoins_billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.android_appcoins_billing.helpers.CatapultBillingAppCoinsFactory;
import com.appcoins.sdk.android_appcoins_billing.types.SkuType;
import com.appcoins.sdk.billing.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.BillingFlowParams;
import com.appcoins.sdk.billing.ConsumeResponseListener;
import com.appcoins.sdk.billing.Purchase;
import com.appcoins.sdk.billing.PurchasesResult;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.SkuDetailsParams;
import com.appcoins.sdk.billing.SkuDetailsResponseListener;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

  private AppcoinsBillingClient cab;
  private CompositeDisposable compositeDisposable;
  private String token = null;
  private AppCoinsBillingStateListener listener;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    compositeDisposable = new CompositeDisposable();
    cab = CatapultBillingAppCoinsFactory.BuildAppcoinsBilling(this.getApplicationContext(),
        BuildConfig.IAB_KEY);

    final Activity activity = this;
    listener = new AppCoinsBillingStateListener() {
      @Override public void onBillingSetupFinished(int responseCode) {
        Log.d("Message: ", "Connected-" + responseCode + "");
      }

      @Override public void onBillingServiceDisconnected() {
        Log.d("Message: ", "Disconnected");
      }
    };
    cab.startConnection(listener);
  }

  @Override protected void onDestroy() {
    compositeDisposable.dispose();
    super.onDestroy();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d("Activity Result: ", "onActivityResult(" + requestCode + "," + resultCode + "," + data);
    if (data != null && data.getExtras() != null) {
      Bundle bundle = data.getExtras();
      if (bundle != null) {
        for (String key : bundle.keySet()) {
          Object value = bundle.get(key);
          if (value != null) {
            Log.d("Message Key", key);
            Log.d("Message value", value.toString());
          }
        }
      }
    }
  }

  public void onBuyGasButtonClicked(View arg0) {
    BillingFlowParams billingFlowParams =
        new BillingFlowParams("gas", SkuType.inapp.toString(), 10001, null, null, null);
    int launchBillingFlowResponse = cab.launchBillingFlow(this, billingFlowParams);
    Log.d("BillingFlowResponse: ", launchBillingFlowResponse + "");
  }

  public void onUpgradeAppButtonClicked(View arg0) {
    PurchasesResult pr = cab.queryPurchases(SkuType.inapp.toString());
    if (pr.getPurchases()
        .size() > 0) {
      for (Purchase p : pr.getPurchases()) {
        Log.d("Purchase result token: ", p.getToken());
        Log.d("Purchase result sku: ", p.getSku());
      }
      token = pr.getPurchases()
          .get(0)
          .getToken();
    } else {
      Log.d("Message:", "No Available Purchases");
    }
  }

  public void onCreateChannelButtonClicked(View view) {
    SkuDetailsParams skuDetailsParams = new SkuDetailsParams();
    skuDetailsParams.setItemType(SkuType.inapp.toString());
    ArrayList<String> al = new ArrayList<String>();

    al.add("gas");

    skuDetailsParams.setMoreItemSkus(al);

    cab.querySkuDetailsAsync(skuDetailsParams, new SkuDetailsResponseListener() {
      @Override
      public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
        Log.d("responseCode: ", responseCode + "");
        for (SkuDetails sd : skuDetailsList) {
          Log.d("SkuDetails: ", sd.getSku() + "");
        }
      }
    });
  }

  public void makePaymentButtonClicked(View view) {
    if (token != null) {
      cab.consumeAsync(token, new ConsumeResponseListener() {
        @Override public void onConsumeResponse(int responseCode, String purchaseToken) {
          Log.d("consume response: ",
              responseCode + " " + "Consumed purchase with token: " + purchaseToken);
          token = null;
        }
      });
    } else {
      Log.d("Message:", "No purchase tokens available");
    }
  }

  public void onCloseChannelButtonClicked(View view) {
    cab.endConnection();
  }

  private boolean checkChannelAvailable() {
    return cab.isReady();
  }

  public void checkChannelAvailable(View view) {
    Toast.makeText(this, "Is Ready: " + checkChannelAvailable(), Toast.LENGTH_SHORT)
        .show();
  }

  public void onOpenChannelButtonClicked(View view) {
    cab.startConnection(listener);
  }
}