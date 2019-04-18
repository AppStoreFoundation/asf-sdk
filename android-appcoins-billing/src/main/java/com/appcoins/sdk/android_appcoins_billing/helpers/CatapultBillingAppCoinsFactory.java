package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.content.Context;
import android.util.Base64;
import com.appcoins.sdk.android_appcoins_billing.AppcoinsBillingClient;
import com.appcoins.sdk.android_appcoins_billing.BuildConfig;
import com.appcoins.sdk.android_appcoins_billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.android_appcoins_billing.RepositoryServiceConnection;
import com.appcoins.sdk.billing.AppCoinsBilling;
import com.appcoins.sdk.billing.GetSkuDetailsService;

public class CatapultBillingAppCoinsFactory {

  public static AppcoinsBillingClient BuildAppcoinsBilling(Context context,
      String base64PublicKey) {
    GetSkuDetailsService getSkuDetailsService = new GetSkuDetailsService(BuildConfig.HOST_WS);

    AppCoinsAndroidBillingRepository repository = new AppCoinsAndroidBillingRepository(3,
        context.getApplicationContext()
            .getPackageName(),
        context.getApplicationContext(), getSkuDetailsService);
    RepositoryServiceConnection connection = new RepositoryServiceConnection(context, repository);

    //Base64 Decoded Public Key
    byte[] base64DecodedPublicKey = Base64.decode(base64PublicKey, Base64.DEFAULT);

    return new CatapultAppcoinsBilling(new AppCoinsBilling(repository, base64DecodedPublicKey),
        connection);
  }
}