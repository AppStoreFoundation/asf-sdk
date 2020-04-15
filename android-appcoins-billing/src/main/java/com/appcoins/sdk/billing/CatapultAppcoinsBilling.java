package com.appcoins.sdk.billing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;
import com.appcoins.sdk.billing.exceptions.ServiceConnectionException;
import com.appcoins.sdk.billing.helpers.AppCoinsPendingIntentCaller;
import com.appcoins.sdk.billing.helpers.EventLogger;
import com.appcoins.sdk.billing.helpers.PayloadHelper;
import com.appcoins.sdk.billing.listeners.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.listeners.ConsumeResponseListener;
import com.appcoins.sdk.billing.listeners.SkuDetailsResponseListener;

public class CatapultAppcoinsBilling implements AppcoinsBillingClient {
  private static final int REQUEST_CODE = 51;

  private final Billing billing;
  private final RepositoryConnection connection;
  private final PurchasesUpdatedListener purchaseFinishedListener;

  public CatapultAppcoinsBilling(Billing billing, RepositoryConnection connection,
      PurchasesUpdatedListener purchaseFinishedListener) {
    this.billing = billing;
    this.connection = connection;
    this.purchaseFinishedListener = purchaseFinishedListener;
  }

  @Override public PurchasesResult queryPurchases(String skuType) {
    return billing.queryPurchases(skuType);
  }

  @Override public void querySkuDetailsAsync(SkuDetailsParams skuDetailsParams,
      SkuDetailsResponseListener onSkuDetailsResponseListener) {
    billing.querySkuDetailsAsync(skuDetailsParams, onSkuDetailsResponseListener);
  }

  @Override
  public void consumeAsync(String token, ConsumeResponseListener consumeResponseListener) {
    billing.consumeAsync(token, consumeResponseListener);
  }

  @Override public int launchBillingFlow(Activity activity, BillingFlowParams billingFlowParams) {

    int responseCode;

    try {
      String payload = PayloadHelper.buildIntentPayload(billingFlowParams.getOrderReference(),
          billingFlowParams.getDeveloperPayload(), billingFlowParams.getOrigin());

      Log.d("Message: ", payload);

      Thread eventLoggerThread = new Thread(new EventLogger(billingFlowParams.getSku(),
          activity.getApplicationContext()
              .getPackageName()));
      eventLoggerThread.start();

      LaunchBillingFlowResult launchBillingFlowResult =
          billing.launchBillingFlow(billingFlowParams, payload);

      responseCode = launchBillingFlowResult.getResponseCode();

      if (responseCode != ResponseCode.OK.getValue()) {
        return responseCode;
      }

      PendingIntent pendingIntent = launchBillingFlowResult.getBuyIntent();
      AppCoinsPendingIntentCaller.startPendingAppCoinsIntent(activity,
          pendingIntent.getIntentSender(), REQUEST_CODE, null, 0, 0, 0);
    } catch (NullPointerException e) {
      e.printStackTrace();
      return ResponseCode.ERROR.getValue();
    } catch (IntentSender.SendIntentException e) {
      e.printStackTrace();
      return ResponseCode.ERROR.getValue();
    } catch (ServiceConnectionException e) {
      e.printStackTrace();
      return ResponseCode.SERVICE_UNAVAILABLE.getValue();
    }
    return ResponseCode.OK.getValue();
  }

  @Override public void startConnection(final AppCoinsBillingStateListener listener) {
    if (!isReady()) {
      connection.startConnection(listener);
    }
  }

  @Override public void endConnection() {
    if (isReady()) {
      connection.endConnection();
    }
  }

  @Override public boolean isReady() {
    return billing.isReady();
  }

  @Override public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE) {
      ApplicationUtils.handleActivityResult(billing, resultCode, data, purchaseFinishedListener);
      return true;
    }
    return false;
  }
}



