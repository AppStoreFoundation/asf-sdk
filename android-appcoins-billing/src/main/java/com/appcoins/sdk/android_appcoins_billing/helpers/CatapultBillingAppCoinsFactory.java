package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import com.appcoins.sdk.android_appcoins_billing.AppcoinsBillingClient;
import com.appcoins.sdk.android_appcoins_billing.BuildConfig;
import com.appcoins.sdk.android_appcoins_billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.android_appcoins_billing.RepositoryServiceConnection;
import com.appcoins.sdk.billing.AppCoinsBilling;
import com.appcoins.sdk.billing.GetSkuDetailsService;
import com.google.gson.JsonParser;

public class CatapultBillingAppCoinsFactory {

  public static AppcoinsBillingClient BuildAppcoinsBilling(Context context,
      String base64PublicKey) {
    boolean isDebug = BuildConfig.DEBUG;
    GetSkuDetailsService getSkuDetailsService;

    if (isDebug) {
      getSkuDetailsService = new GetSkuDetailsService(BuildConfig.ROPSTEN_NETWORK_BACKEND_BASE_HOST_WS);
    } else {
      getSkuDetailsService = new GetSkuDetailsService(BuildConfig.MAIN_NETWORK_BACKEND_BASE_HOST_WS);
    }

    AppCoinsAndroidBillingRepository repository = new AppCoinsAndroidBillingRepository(3,
        context.getApplicationContext()
            .getPackageName(), new AndroidBillingMapper(new JsonParser()),
        context.getApplicationContext(), getSkuDetailsService);
    RepositoryServiceConnection connection = new RepositoryServiceConnection(context, repository);

    //Base64 Decoded Public Key
    byte[] base64DecodedPublicKey = Base64.decode(base64PublicKey, Base64.DEFAULT);

    return new CatapultAppcoinsBilling(new AppCoinsBilling(repository, base64DecodedPublicKey),
        connection);
  }
}