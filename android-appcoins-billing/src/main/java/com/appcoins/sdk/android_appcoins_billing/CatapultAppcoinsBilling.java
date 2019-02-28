package com.appcoins.sdk.android_appcoins_billing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;
import com.appcoins.sdk.android_appcoins_billing.helpers.PayloadHelper;
import com.appcoins.sdk.billing.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.Billing;
import com.appcoins.sdk.billing.BillingFlowParams;
import com.appcoins.sdk.billing.ConsumeResponseListener;
import com.appcoins.sdk.billing.ResponseCode;
import com.appcoins.sdk.billing.LaunchBillingFlowResult;
import com.appcoins.sdk.billing.PurchasesResult;
import com.appcoins.sdk.billing.ServiceConnectionException;
import com.appcoins.sdk.billing.SkuDetailsParams;
import com.appcoins.sdk.billing.SkuDetailsResponseListener;

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

  public int launchBillingFlow(Activity activity, BillingFlowParams billingFlowParams) {
    try {
      String payload = PayloadHelper.buildIntentPayload(billingFlowParams.getOrderReference(),
          billingFlowParams.getDeveloperPayload(), billingFlowParams.getOrigin());

      Log.d("Message: ", payload);

      LaunchBillingFlowResult launchBillingFlowResult =
          billing.launchBillingFlow(billingFlowParams, payload);

      PendingIntent pendingIntent = (PendingIntent) launchBillingFlowResult.getBuyIntent();

      activity.startIntentSenderForResult(pendingIntent.getIntentSender(),
          billingFlowParams.getRequestCode(), new Intent(), 0, 0, 0);
    } catch (NullPointerException e) {
      return ResponseCode.ERROR.getValue();
    } catch (IntentSender.SendIntentException e) {
      return ResponseCode.ERROR.getValue();
    } catch (ServiceConnectionException e) {
      return ResponseCode.SERVICE_UNAVAILABLE.getValue();
    }
    return ResponseCode.OK.getValue();
  }

  public void startConnection(final AppCoinsBillingStateListener listener) {
    connection.startConnection(listener);
  }
}



