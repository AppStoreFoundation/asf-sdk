package com.appcoins.sdk.billing.helpers;

import android.content.Context;
import android.util.Base64;
import com.appcoins.sdk.android.billing.BuildConfig;
import com.appcoins.sdk.billing.AppCoinsBilling;
import com.appcoins.sdk.billing.AppcoinsBillingClient;
import com.appcoins.sdk.billing.CatapultAppcoinsBilling;
import com.appcoins.sdk.billing.RepositoryServiceConnection;
import com.appcoins.sdk.billing.WSServiceController;

public class CatapultBillingAppCoinsFactory {

  public static AppcoinsBillingClient BuildAppcoinsBilling(Context context,
      String base64PublicKey) {

    WSServiceController.setWsUrl(BuildConfig.HOST_WS);

    AppCoinsAndroidBillingRepository repository = new AppCoinsAndroidBillingRepository(3,
        context.getApplicationContext()
            .getPackageName(), context.getApplicationContext());

    RepositoryServiceConnection connection = new RepositoryServiceConnection(context, repository);

    WalletUtils.setContext(context);

    //Base64 Decoded Public Key
    byte[] base64DecodedPublicKey = Base64.decode(base64PublicKey, Base64.DEFAULT);

    return new CatapultAppcoinsBilling(new AppCoinsBilling(repository, base64DecodedPublicKey),
        connection);
  }
}