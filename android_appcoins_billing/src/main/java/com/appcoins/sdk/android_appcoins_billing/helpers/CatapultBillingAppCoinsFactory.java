package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.content.Context;
import com.appcoins.sdk.android_appcoins_billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.android_appcoins_billing.RepositoryServiceConnection;
import com.appcoins.sdk.billing.AppCoinsBilling;
import com.google.gson.Gson;

public class CatapultBillingAppCoinsFactory {

  public static CatapultAppcoinsBilling BuildAppcoinsBilling(Context context,
      String base64PublicKey) {

    AppCoinsAndroidBillingRepository repository = new AppCoinsAndroidBillingRepository(3,
        context.getApplicationContext()
            .getPackageName(), new AndroidBillingMapper(new Gson()));
    RepositoryServiceConnection connection = new RepositoryServiceConnection(context, repository);
    ///IabHelper iabHelper = new IabHelper(context, base64PublicKey);

    return new CatapultAppcoinsBilling(new AppCoinsBilling(repository), connection);
  }
}

