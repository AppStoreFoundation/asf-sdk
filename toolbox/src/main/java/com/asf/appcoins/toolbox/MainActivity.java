package com.asf.appcoins.toolbox;

import android.app.Activity;
import android.app.AlertDialog;
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
import java.util.Locale;

public class MainActivity extends Activity {

  private static final String TAG = MainActivity.class.getSimpleName();
  private AppcoinsBillingClient cab;
  private String token = null;
  private AppCoinsBillingStateListener listener;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    PurchasesUpdatedListener purchaseFinishedListener = (responseCode, purchases) -> {
      if (responseCode == ResponseCode.OK.getValue()) {
        for (Purchase purchase : purchases) {
          token = purchase.getToken();
        }
      } else {
        new AlertDialog.Builder(this).setMessage(
            String.format(Locale.ENGLISH, "response code: %d -> %s", responseCode,
                ResponseCode.values()[responseCode].name()))
            .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
            .create()
            .show();
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
        new BillingFlowParams("gas", SkuType.inapp.toString(), null, null, null);

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

    skusList.add("upgrade.hero.kshaat.3");
    skusList.add("upgrade.hero.kharanna.3");
    skusList.add("upgrade.hero.gassar.1");
    skusList.add("offer.hargrim.2.1");
    skusList.add("energypack1.2");
    skusList.add("offer.maedb.50");
    skusList.add("hero.cash.xaart.30");
    skusList.add("upgrade.hero.sterus.1");
    skusList.add("starterpack.cny.3");
    skusList.add("finisher.cny.1");
    skusList.add("com.playkot.ageofmagic.weekly5");
    skusList.add("upgrade.hero.arechonspear.3");
    skusList.add("upgrade.hero.xaart.3");
    skusList.add("hero.cash.rizer.30");
    skusList.add("upgrade.hero.angrim.1");
    skusList.add("upgrade.hero.denaya.1");
    skusList.add("upgrade.hero.teeros.1");
    skusList.add("hero.cash.hargrim.30");
    skusList.add("com.playkot.ageofmagic.gold15710");
    skusList.add("hero.cash.atiles.30");
    skusList.add("hero.cash.abyssalhound.30");
    skusList.add("kobold.slinger.40");
    skusList.add("starterpack.cny.2");
    skusList.add("com.playkot.ageofmagic.monthly1");
    skusList.add("offer.sterus.50");
    skusList.add("upgrade.hero.bloodpriest.3");
    skusList.add("silveroffer1.30.3");
    skusList.add("upgrade.hero.arechonguardsman.1");
    skusList.add("cheapstarterpack2.large");
    skusList.add("upgrade.hero.gatekeeper.3");
    skusList.add("upgrade.hero.koboldspear.3");
    skusList.add("upgrade.hero.taneda.2");
    skusList.add("starterpack.lucky.2");
    skusList.add("offer.blackfriday.30.5");
    skusList.add("upgrade.hero.gatekeeper.1");
    skusList.add("upgrade.hero.koboldspear.2");
    skusList.add("offer.xaart.1");
    skusList.add("offer.lucky.50");
    skusList.add("upgrade.hero.sabertooth.1");
    skusList.add("upgrade.hero.softy.1");
    skusList.add("upgrade.hero.tahit.1");
    skusList.add("upgrade.hero.tsuna.1");
    skusList.add("offer.teeros.50");
    skusList.add("hero.cash.burner.30");
    skusList.add("upgrade.hero.abyssalhound.1");
    skusList.add("upgrade.hero.shadow.1");
    skusList.add("hero.cash.sterus.30");
    skusList.add("com.playkot.ageofmagic.weekly6");
    skusList.add("upgrade.hero.arechonspear.1");
    skusList.add("hero.cash.bloodemperor.30");
    skusList.add("upgrade.hero.shadow.3");
    skusList.add("upgrade.hero.bellara.1");
    skusList.add("upgrade.hero.sunwukong.1.2");
    skusList.add("upgrade.hero.eraser.3");
    skusList.add("kobold.guard.40");
    skusList.add("com.playkot.ageofmagic.weekly1");
    skusList.add("hero.cash.denaya.30");
    skusList.add("com.playkot.ageofmagic.monthly6");
    skusList.add("hero.cash.arechonaxethrower.30");
    skusList.add("upgrade.hero.koboldguard.3");
    skusList.add("upgrade.hero.taneda.3");
    skusList.add("com.playkot.ageofmagic.monthly2");
    skusList.add("upgrade.hero.koboldsoothsayer.1");
    skusList.add("silveroffer1.30.2");
    skusList.add("upgrade.hero.koboldpriest.1");
    skusList.add("hero.cash.softy.30");
    skusList.add("upgrade.hero.sunwukong.1");
    skusList.add("upgrade.hero.bloodmage.1");
    skusList.add("offer.blackfriday.1.1");
    skusList.add("hero.cash.dreverad.30");
    skusList.add("upgrade.hero.abaddon.1");
    skusList.add("upgrade.hero.taneda.1");
    skusList.add("upgrade.hero.teeros.3");
    skusList.add("offer.xaart.2.1");
    skusList.add("offer.blackfriday.30.7");
    skusList.add("upgrade.hero.balthazar.1");
    skusList.add("upgrade.hero.koboldscout.2");
    skusList.add("upgrade.hero.myrddin.1");
    skusList.add("upgrade.hero.kharanna.1");
    skusList.add("upgrade.hero.zhubajie.1");
    skusList.add("silveroffer2.35.1");
    skusList.add("offer.hargrim.2.2");
    skusList.add("kobolock.40");
    skusList.add("upgrade.hero.koboldsoothsayer.2");
    skusList.add("hero.cash.sabertooth.30");
    skusList.add("offer.xaart.large.2.2");
    skusList.add("hero.cash.mizuhiko.30");
    skusList.add("com.playkot.ageofmagic.year6");
    skusList.add("com.playkot.ageofmagic.year4");
    skusList.add("upgrade.hero.silvermoon.1");
    skusList.add("offer.hargrim.large.2.1");
    skusList.add("upgrade.hero.koboldscout.1");
    skusList.add("upgrade.hero.raarspit.1");
    skusList.add("offer.abaddon.50");
    skusList.add("com.playkot.ageofmagic.gold2800");
    skusList.add("cheapstarterpack1");
    skusList.add("hero.cash.bloodmage.30");
    skusList.add("upgrade.hero.koboldpriest.2");
    skusList.add("upgrade.hero.rizer.1");
    skusList.add("hero.cash.swampkiller.30");
    skusList.add("hero.cash.angrim.30");
    skusList.add("upgrade.hero.kshaat.1");
    skusList.add("offer.blackfriday.1.3");
    skusList.add("com.playkot.ageofmagic.weekly4");
    skusList.add("hero.cash.gatekeeper.30");
    skusList.add("upgrade.hero.kobolock.2");
    skusList.add("kobold.scout.40");
    skusList.add("hero.cash.abaddon.30");
    skusList.add("upgrade.hero.bloodpriest.1");
    skusList.add("upgrade.hero.arechonaxe.3");
    skusList.add("silveroffer1.70.3");
    skusList.add("com.playkot.ageofmagic.year1");
    skusList.add("hero.cash.cathbad.30");
    skusList.add("hero.cash.kshaat.30");
    skusList.add("offer.sunwukong.50");
    skusList.add("upgrade.hero.ambror.3");
    skusList.add("upgrade.hero.rok.1");
    skusList.add("energypack1.3");
    skusList.add("silveroffer2.35.2");
    skusList.add("starterpack.cny.1");
    skusList.add("upgrade.hero.aratar.1");
    skusList.add("upgrade.hero.arechonguardsman.3");
    skusList.add("com.playkot.ageofmagic.monthly3");
    skusList.add("starterpack.lucky.1");
    skusList.add("upgrade.hero.velundar.1");
    skusList.add("hero.cash.silvermoon.30");
    skusList.add("com.playkot.ageofmagic.gold7370");
    skusList.add("upgrade.hero.sacrif.1");
    skusList.add("silveroffer1.70.2");
    skusList.add("offer.blackfriday.1.4");
    skusList.add("hero.cash.akhrasht.30");
    skusList.add("upgrade.hero.ambror.1");
    skusList.add("energypack1.1");
    skusList.add("starterpack.azariel.1");
    skusList.add("upgrade.hero.koboldscout.3");
    skusList.add("skins.cny.1");
    skusList.add("upgrade.hero.raarspit.3");
    skusList.add("upgrade.hero.sevenknifes.1");
    skusList.add("com.playkot.ageofmagic.weekly3");
    skusList.add("hero.cash.velundar.30");
    skusList.add("offer.blackfriday.1.6");
    skusList.add("upgrade.hero.swampkiller.1");
    skusList.add("upgrade.hero.koboldslinger.1");
    skusList.add("hero.cash.ambror.30");
    skusList.add("hero.cash.eraser.30");
    skusList.add("upgrade.hero.kage.1");
    skusList.add("upgrade.hero.succubus.1");
    skusList.add("upgrade.hero.siegfried.1");
    skusList.add("hero.cash.lucky.30");
    skusList.add("silveroffer1.30.1");
    skusList.add("upgrade.hero.aratar.3");
    skusList.add("offer.blackfriday.30.6");
    skusList.add("com.playkot.ageofmagic.year5");
    skusList.add("com.playkot.ageofmagic.year2");
    skusList.add("upgrade.hero.hilia.1");
    skusList.add("upgrade.hero.burner.3");
    skusList.add("upgrade.hero.lucky.3");
    skusList.add("hero.cash.gassar.30");
    skusList.add("upgrade.hero.maedb.1");
    skusList.add("upgrade.hero.koboldspear.1");
    skusList.add("silveroffer1.50.2");
    skusList.add("upgrade.hero.rok.2");
    skusList.add("hero.cash.bellara.30");
    skusList.add("upgrade.hero.koboldsoothsayer.3");
    skusList.add("offer.blackfriday.30.4");
    skusList.add("offer.hargrim.large.2.2");
    skusList.add("com.playkot.ageofmagic.monthly4");
    skusList.add("upgrade.hero.koboldguard.2");
    skusList.add("hero.cash.aratar.30");
    skusList.add("hero.cash.bloodpriest.30");
    skusList.add("upgrade.hero.eraser.1");
    skusList.add("starterpack.halloween2019.1");
    skusList.add("upgrade.hero.burner.1");
    skusList.add("com.playkot.ageofmagic.gold1340");
    skusList.add("upgrade.hero.bellara.3");
    skusList.add("upgrade.hero.atiles.1");
    skusList.add("offer.blackfriday.30.2");
    skusList.add("cheapstarterpack2.small");
    skusList.add("upgrade.hero.kobolock.1");
    skusList.add("hero.cash.hilia.30");
    skusList.add("offer.xaart.large.1");
    skusList.add("kobold.soothsayer.40");
    skusList.add("hero.cash.teeros.30");
    skusList.add("offer.hargrim.large.1");
    skusList.add("upgrade.hero.arechonaxe.1");
    skusList.add("upgrade.hero.bloodemperor.1");
    skusList.add("silveroffer1.70.1");
    skusList.add("silveroffer2.35.3");
    skusList.add("hero.cash.shadow.30");
    skusList.add("upgrade.hero.cathbad.1");
    skusList.add("com.playkot.ageofmagic.weekly2");
    skusList.add("upgrade.hero.mizuhiko.1");
    skusList.add("starterpack.halloween2019.2");
    skusList.add("silveroffer1.50.1");
    skusList.add("silveroffer1.50.3");
    skusList.add("upgrade.hero.hargrim.3");
    skusList.add("offer.xaart.2.2");
    skusList.add("upgrade.hero.xaart.1");
    skusList.add("offer.xaart.large.2.1");
    skusList.add("hero.cash.tahit.30");
    skusList.add("starterpack.azariel.2");
    skusList.add("offer.hargrim.1");
    skusList.add("offer.zhubajie.50");
    skusList.add("offer.azariel.50");
    skusList.add("com.playkot.ageofmagic.year3");
    skusList.add("offer.blackfriday.1.5");
    skusList.add("hero.cash.troddar.30");
    skusList.add("upgrade.hero.sterus.3");
    skusList.add("upgrade.hero.akrasht.3");
    skusList.add("energypack1.4");
    skusList.add("upgrade.hero.kobolock.3");
    skusList.add("hero.cash.elios.30");
    skusList.add("upgrade.hero.troddar.1");
    skusList.add("kobold.spear.40");
    skusList.add("upgrade.hero.healerhargrim.1");
    skusList.add("hero.cash.arechonspear.30");
    skusList.add("upgrade.hero.bloodmage.3");
    skusList.add("upgrade.hero.koboldguard.1");
    skusList.add("upgrade.hero.abyssalhound.3");
    skusList.add("upgrade.hero.koboldslinger.2");
    skusList.add("offer.blackfriday.1.2");
    skusList.add("upgrade.hero.koboldslinger.3");
    skusList.add("upgrade.hero.rogar.3");
    skusList.add("upgrade.hero.dreverad.1");
    skusList.add("offer.blackfriday.30.3");
    skusList.add("hero.cash.raarspit.30");
    skusList.add("hero.cash.arechonguardsman.30");
    skusList.add("raid.cny.1");
    skusList.add("upgrade.hero.elios.1");
    skusList.add("com.playkot.ageofmagic.monthly5");
    skusList.add("upgrade.hero.akhrasht.1");
    skusList.add("com.playkot.ageofmagic.gold610");
    skusList.add("com.playkot.ageofmagic.gold220");

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