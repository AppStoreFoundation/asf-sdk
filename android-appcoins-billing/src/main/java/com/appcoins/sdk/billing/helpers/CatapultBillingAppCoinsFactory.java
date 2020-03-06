package com.appcoins.sdk.billing.helpers;

import android.content.Context;
import android.util.Base64;
import com.appcoins.sdk.billing.AppCoinsBilling;
import com.appcoins.sdk.billing.AppcoinsBillingClient;
import com.appcoins.sdk.billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.billing.PurchasesUpdatedListener;
import com.appcoins.sdk.billing.RepositoryServiceConnection;

public class CatapultBillingAppCoinsFactory {

  public static AppcoinsBillingClient BuildAppcoinsBilling(Context context, String base64PublicKey,
      PurchasesUpdatedListener purchaseFinishedListener) {

    AppCoinsAndroidBillingRepository repository =
        new AppCoinsAndroidBillingRepository(3, context.getPackageName());

    RepositoryServiceConnection connection =
        new RepositoryServiceConnection(context.getApplicationContext(), repository);
    WalletUtils.setContext(context.getApplicationContext());

    byte[] base64DecodedPublicKey = Base64.decode(base64PublicKey, Base64.DEFAULT);

    return new CatapultAppcoinsBilling(new AppCoinsBilling(repository, base64DecodedPublicKey),
        connection, purchaseFinishedListener);
  }
}