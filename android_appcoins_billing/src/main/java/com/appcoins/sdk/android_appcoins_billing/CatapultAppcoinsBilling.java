package com.appcoins.sdk.android_appcoins_billing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import com.appcoins.sdk.android_appcoins_billing.helpers.PayloadHelper;
import com.appcoins.sdk.billing.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.Billing;
import com.appcoins.sdk.billing.BillingFlowParams;
import com.appcoins.sdk.billing.ConsumeResponseListener;
import com.appcoins.sdk.billing.PurchasesResult;
import com.appcoins.sdk.billing.ServiceConnectionException;
import com.appcoins.sdk.billing.SkuDetailsParams;
import com.appcoins.sdk.billing.SkuDetailsResponseListener;
import java.util.HashMap;

public class CatapultAppcoinsBilling {

  private final Billing billing;
  private final RepositoryConnection connection;

  public CatapultAppcoinsBilling(Billing billing, RepositoryConnection connection) {
    this.billing = billing;
    this.connection = connection;
  }

  public PurchasesResult queryPurchases(String skuType) {
    return billing.queryPurchases(skuType);
  }

  public void querySkuDetailsAsync(SkuDetailsParams skuDetailsParams,
      SkuDetailsResponseListener onSkuDetailsResponseListener) {
    billing.querySkuDetailsAsync(skuDetailsParams, onSkuDetailsResponseListener);
  }

  public void consumeAsync(String token, ConsumeResponseListener consumeResponseListener) {
    billing.consumeAsync(token, consumeResponseListener);
  }

  public void lauchBillingFlow(Activity activity, BillingFlowParams billingFlowParams) {
    try {
      String payload = PayloadHelper.buildIntentPayload(billingFlowParams.getOrderReference(),
          billingFlowParams.getDeveloperPayload(), billingFlowParams.getOrigin());

      HashMap<String, Object> hashMap = billing.launchBillingFlow(billingFlowParams, payload);

      PendingIntent pendingIntent = (PendingIntent) hashMap.get("BUY_INTENT");
      activity.startIntentSenderForResult(pendingIntent.getIntentSender(),
          billingFlowParams.getRequestCode(), new Intent(), 0, 0, 0);
    } catch (IntentSender.SendIntentException e) {
      e.printStackTrace();
    } catch (ServiceConnectionException e) {
      e.printStackTrace();
    }
  }

  public void startService(final AppCoinsBillingStateListener listener) {
    connection.startService(listener);
  }
}



