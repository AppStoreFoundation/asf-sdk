package com.asf.appcoins.sdk.ads.campaign.manager;

import com.asf.appcoins.sdk.ads.campaign.Campaign;
import com.asf.appcoins.sdk.ads.campaign.contract.CampaignContract;
import java.util.List;

public class CampaignManager {

  private final CampaignContract campaignContract;

  public CampaignManager() {
    this(new DummyContract());
  }

  public CampaignManager(CampaignContract campaignContract) {
    this.campaignContract = campaignContract;
  }

  public List<Campaign> getActiveCampaings(String packageName) {
    return campaignContract.getActiveCampaings(packageName);
  }
}
