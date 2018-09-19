package com.asf.appcoins.sdk.ads.poa.campaign;

import io.reactivex.Single;

public interface CampaignService {
  Single<Campaign> getCampaign();
}
