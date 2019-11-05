package com.asf.appcoins.toolbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.appcoins.sdk.billing.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.AppcoinsBillingClient;
import com.appcoins.sdk.billing.BillingFlowParams;
import com.appcoins.sdk.billing.ConsumeResponseListener;
import com.appcoins.sdk.billing.Purchase;
import com.appcoins.sdk.billing.PurchasesResult;
import com.appcoins.sdk.billing.PurchasesUpdatedListener;
import com.appcoins.sdk.billing.ResponseCode;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.SkuDetailsParams;
import com.appcoins.sdk.billing.SkuDetailsResponseListener;
import com.appcoins.sdk.billing.helpers.CatapultBillingAppCoinsFactory;
import com.appcoins.sdk.billing.types.SkuType;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

  private static final String TAG = MainActivity.class.getSimpleName();
  private AppcoinsBillingClient cab;
  private String token = null;
  private AppCoinsBillingStateListener listener;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    PurchasesUpdatedListener purchaseFinishedListener = (responseCode, purchases) -> {
      if (responseCode== ResponseCode.OK.getValue()) {
        for (Purchase purchase : purchases) {
          token = purchase.getToken();
        }
      }
    };
    cab = CatapultBillingAppCoinsFactory.BuildAppcoinsBilling(this, BuildConfig.IAB_KEY,
        purchaseFinishedListener);

    listener = new AppCoinsBillingStateListener() {
      @Override public void onBillingSetupFinished(int responseCode) {
        Log.d(TAG, "Is Billing Setup Finished:  Connected-" + responseCode + "");
      }

      @Override public void onBillingServiceDisconnected() {
        Log.d(TAG, "Message: Disconnected");
      }
    };
    cab.startConnection(listener);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d(TAG,
        "Activity Result: onActivityResult(" + requestCode + "," + resultCode + "," + data + ")");
    cab.onActivityResult(requestCode, resultCode, data);
    if (data != null && data.getExtras() != null) {
      Bundle bundle = data.getExtras();
      if (bundle != null) {
        for (String key : bundle.keySet()) {
          Object value = bundle.get(key);
          if (value != null) {
            Log.d(TAG, "Message Key: " + key);
            Log.d(TAG, "Message value: " + value.toString());
          }
        }
      }
    }
  }

  public void onBuyGasButtonClicked(View arg0) {
    BillingFlowParams billingFlowParams =
        new BillingFlowParams("gas", SkuType.inapp.toString(), null, "cenas", null);

    Activity act = this;
    Thread t = new Thread(new Runnable() {
      @Override public void run() {
        int launchBillingFlowResponse = cab.launchBillingFlow(act, billingFlowParams);
        Log.d(TAG, "BillingFlowResponse: " + launchBillingFlowResponse);
      }
    });
    t.start();
  }

  public void onUpgradeAppButtonClicked(View arg0) {

    Thread t = new Thread(new Runnable() {
      @Override public void run() {
        PurchasesResult pr = cab.queryPurchases(SkuType.inapp.toString());
        if (pr.getPurchases()
            .size() > 0) {
          for (Purchase p : pr.getPurchases()) {
            Log.d(TAG, "Purchase result token: " + p.getToken());
            Log.d(TAG, "Purchase result sku: " + p.getSku());
          }
          token = pr.getPurchases()
              .get(0)
              .getToken();
        } else {
          Log.d(TAG, "Message: No Available Purchases");
        }
      }
    });
    t.start();
  }

  public void onSkuDetailsButtonClicked(View view) {
    SkuDetailsParams skuDetailsParams = new SkuDetailsParams();
    skuDetailsParams.setItemType(SkuType.inapp.toString());
    ArrayList<String> skusList = new ArrayList<String>();

    skusList.add("gas");

    skuDetailsParams.setMoreItemSkus(skusList);

    Thread t = new Thread(new Runnable() {
      @Override public void run() {
        cab.querySkuDetailsAsync(skuDetailsParams, new SkuDetailsResponseListener() {
          @Override
          public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
            Log.d(TAG, "responseCode: " + responseCode + "");
            for (SkuDetails sd : skuDetailsList) {
              Log.d(TAG, sd.toString());
            }
          }
        });
      }
    });

    t.start();
  }

  public void makePaymentButtonClicked(View view) {

    Thread t = new Thread(new Runnable() {
      @Override public void run() {

        if (token != null) {
          cab.consumeAsync(token, new ConsumeResponseListener() {
            @Override public void onConsumeResponse(int responseCode, String purchaseToken) {
              Log.d(TAG, "consume response: "
                  + responseCode
                  + " "
                  + "Consumed purchase with token: "
                  + purchaseToken);
              token = null;
            }
          });
        } else {
          Log.d(TAG, "Message: No purchase tokens available");
        }
      }
    });

    t.start();
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