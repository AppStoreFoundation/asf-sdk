package com.asf.appcoins.toolbox;

import android.app.Application;
import com.asf.appcoins.sdk.iab.entity.SKU;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class App extends Application {

  private static final String DEVELOPER_ADDRESS = "0x2c30194bd2e7b6b8ff1467c5af1650f53cd231be";

  @Override public void onCreate() {
    super.onCreate();

    AppCoinsIabSingleton.create(DEVELOPER_ADDRESS, buildSkus(), true);
    AdvertisementSdkSingleton.create(this, true);
    AdvertisementSdkSingleton.getAdsSdk()
        .init(this);
  }

  private List<SKU> buildSkus() {
    List<SKU> skus = new LinkedList<>();

    skus.add(new SKU(Skus.SKU_GAS_LABEL, Skus.SKU_GAS_ID, BigDecimal.valueOf(0.01)));
    skus.add(new SKU(Skus.SKU_PREMIUM_LABEL, Skus.SKU_PREMIUM_ID, BigDecimal.valueOf(0.02)));

    return skus;
  }
}
