package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.content.Context;
import com.appcoins.sdk.android_appcoins_billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.android_appcoins_billing.RepositoryServiceConnection;
import com.appcoins.sdk.billing.AppCoinsBilling;
import com.google.gson.JsonParser;

public class CatapultBillingAppCoinsFactory {

  public static CatapultAppcoinsBilling BuildAppcoinsBilling(Context context,
      String base64PublicKey) {

    AppCoinsAndroidBillingRepository repository = new AppCoinsAndroidBillingRepository(3,
        context.getApplicationContext()
            .getPackageName(), new AndroidBillingMapper(new JsonParser()));
    RepositoryServiceConnection connection = new RepositoryServiceConnection(context, repository);

    return new CatapultAppcoinsBilling(new AppCoinsBilling(repository), connection);
  }
}

