package com.appcoins.sdk.billing.payasguest;

import android.os.AsyncTask;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.WSServiceController;
import com.appcoins.sdk.billing.helpers.AndroidBillingMapper;
import java.util.ArrayList;
import java.util.List;

class SingleSkuDetailsAsync extends AsyncTask<Object, Object, SkuDetails> {

  private final BuyItemProperties buyItemProperties;
  private final SingleSkuDetailsListener listener;

  public SingleSkuDetailsAsync(BuyItemProperties buyItemProperties,
      SingleSkuDetailsListener listener) {

    this.buyItemProperties = buyItemProperties;
    this.listener = listener;
  }

  @Override protected SkuDetails doInBackground(Object[] objects) {
    SkuDetails skuDetails =
        getSkuDetails(buyItemProperties.getPackageName(), buyItemProperties.getSku(),
            buyItemProperties.getType());
    return skuDetails;
  }

  @Override protected void onPostExecute(SkuDetails skuDetails) {
    if (skuDetails == null) {
      listener.onResponse(true, null);
    } else {
      listener.onResponse(false, skuDetails);
    }
  }

  private SkuDetails getSkuDetails(String packageName, String sku, String type) {
    List<String> skuList = new ArrayList<>();
    skuList.add(sku);
    String response =
        WSServiceController.getSkuDetailsService(BuildConfig.HOST_WS, packageName, skuList);
    return AndroidBillingMapper.mapSingleSkuDetails(type, response);
  }
}
