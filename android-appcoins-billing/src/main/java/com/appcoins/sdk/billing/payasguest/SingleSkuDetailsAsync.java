package com.appcoins.sdk.billing.payasguest;

import android.os.AsyncTask;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.SkuDetails;
import com.appcoins.sdk.billing.WSServiceController;
import com.appcoins.sdk.billing.helpers.AndroidBillingMapper;
import com.appcoins.sdk.billing.helpers.WalletUtils;
import com.appcoins.sdk.billing.listeners.SingleSkuDetailsListener;
import java.util.ArrayList;
import java.util.List;

class SingleSkuDetailsAsync extends AsyncTask<Object, Object, SkuDetails> {

  private final BuyItemProperties buyItemProperties;
  private final SingleSkuDetailsListener listener;

  SingleSkuDetailsAsync(BuyItemProperties buyItemProperties, SingleSkuDetailsListener listener) {

    this.buyItemProperties = buyItemProperties;
    this.listener = listener;
  }

  @Override protected SkuDetails doInBackground(Object[] objects) {
    return getSkuDetails(buyItemProperties.getPackageName(), buyItemProperties.getSku(),
        buyItemProperties.getType());
  }

  @Override protected void onPostExecute(SkuDetails skuDetails) {
    if (skuDetails == null || skuDetails.getFiatPrice()
        .equals("")) {
      listener.onResponse(true, null);
    } else {
      listener.onResponse(false, skuDetails);
    }
  }

  private SkuDetails getSkuDetails(String packageName, String sku, String type) {
    List<String> skuList = new ArrayList<>();
    skuList.add(sku);
    String response =
        WSServiceController.getSkuDetailsService(BuildConfig.HOST_WS, packageName, skuList,
            WalletUtils.getUserAgent());
    return AndroidBillingMapper.mapSingleSkuDetails(type, response);
  }
}
