package com.asf.appcoins.sdk.ads.campaign.contract;

import com.asf.appcoins.sdk.ads.campaign.Campaign;
import java.util.List;

public interface CampaignContract {

  List<Campaign> getActiveCampaings(String packageName);
}
