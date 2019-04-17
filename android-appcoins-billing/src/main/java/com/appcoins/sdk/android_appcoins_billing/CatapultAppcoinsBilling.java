package com.appcoins.sdk.android_appcoins_billing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;
import com.appcoins.sdk.android_appcoins_billing.helpers.DialogVisibleListener;
import com.appcoins.sdk.android_appcoins_billing.helpers.PayloadHelper;
import com.appcoins.sdk.android_appcoins_billing.helpers.WalletUtils;
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

public class CatapultAppcoinsBilling implements AppcoinsBillingClient , DialogVisibleListener {

  private final Billing billing;
  private final RepositoryConnection connection;
  private boolean dialogVisible;

  public CatapultAppcoinsBilling(Billing billing, RepositoryConnection connection) {
    this.billing = billing;
    this.connection = connection;
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

    if (!WalletUtils.hasWalletInstalled(activity.getApplicationContext())) {
      dialogVisible = true;
      WalletUtils.promptToInstallWallet(activity, activity,
          activity.getString(R.string.install_wallet_from_iab), this);
      return ResponseCode.OK.getValue();
    }


    int responseCode;
    try {
      String payload = PayloadHelper.buildIntentPayload(billingFlowParams.getOrderReference(),
          billingFlowParams.getDeveloperPayload(), billingFlowParams.getOrigin());

      Log.d("Message: ", payload);

      LaunchBillingFlowResult launchBillingFlowResult =
          billing.launchBillingFlow(billingFlowParams, payload);

      responseCode = (int)launchBillingFlowResult.getResponseCode();

      if(responseCode != ResponseCode.OK.getValue()){
        return responseCode;
      }

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

  @Override public void OnDialogVisibleListener(boolean value) {
    dialogVisible = value;
  }
}



