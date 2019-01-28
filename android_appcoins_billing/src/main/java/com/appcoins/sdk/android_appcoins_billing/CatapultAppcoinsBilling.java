package com.appcoins.sdk.android_appcoins_billing;

import com.appcoins.sdk.billing.AppCoinsBillingStateListener;
import com.appcoins.sdk.billing.Billing;
import com.appcoins.sdk.billing.PurchasesResult;

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

  /*
  public void querySkuDetailsAsync(SkuDetailsParam skuDetailsParam,
      ResponseListener onSkuDetailsResponseListener) {
    try {
      iabHelper.querySkuDetailsAsync(skuDetailsParam,
          (OnSkuDetailsResponseListener) onSkuDetailsResponseListener);
    } catch (IabAsyncInProgressException e) {
      Log.e("Message: ", "Error querying inventory. Another async operation in progress.");
    }
  }

  public void launchPurchaseFlow(Object act, String sku, String itemType, List<String> oldSkus,
      int requestCode, ResponseListener listener, String extraData) {
    try {
      iabHelper.launchPurchaseFlow((Activity) act, sku, itemType, oldSkus, requestCode,
          (OnIabPurchaseFinishedListener) listener, extraData);
    } catch (IabAsyncInProgressException e) {

    }
  }
  */
  public void startService(final AppCoinsBillingStateListener listener) {
    connection.startService(listener);
  }
}



