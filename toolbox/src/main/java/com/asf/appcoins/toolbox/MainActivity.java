package com.asf.appcoins.toolbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.appcoins.sdk.android_appcoins_billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.android_appcoins_billing.CatapultBillingAppcoinsFactory;
import com.appcoins.sdk.android_appcoins_billing.IabException;
import com.appcoins.sdk.android_appcoins_billing.IabHelper;
import com.appcoins.sdk.android_appcoins_billing.SkuType;
import com.appcoins.sdk.android_appcoins_billing.Utils;
import com.appcoins.sdk.billing.Purchase;
import com.appcoins.sdk.billing.PurchasesResult;

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
        Purchase o = null;
        try {
            o = new Purchase("id", "itemType", "jsonPurchaseInfo","signature");
        } catch (Exception e) {
            e.printStackTrace();
        }
        cab.consumePurchase(o);
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



