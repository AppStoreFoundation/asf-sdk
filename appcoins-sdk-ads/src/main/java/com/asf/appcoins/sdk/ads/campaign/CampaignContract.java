package com.asf.appcoins.sdk.ads.campaign;

import java.util.List;

public interface CampaignContract {

  List<Campaign> getActiveCampaings(String packageName);
}
