package com.asf.appcoins.sdk.ads.network.listeners;

import android.os.Bundle;
import com.asf.appcoins.sdk.ads.network.responses.AppCoinsClientResponse;

public interface GetCampaignResponseListener {
  void responseGetCampaign(AppCoinsClientResponse appCoinsClientResponse);
  void responseGetCampaignWallet(Bundle response);
}
