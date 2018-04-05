package com.asf.appcoins.sdk.ads.campaign.manager;

import com.asf.appcoins.sdk.ads.campaign.Campaign;
import com.asf.appcoins.sdk.ads.campaign.contract.CampaignContract;
import java.util.List;

public class CampaignManager {

  private final CampaignContract campaignContract;
  private final String country;

  public CampaignManager() {
    this(new DummyContract(), "PT");
  }

  public CampaignManager(CampaignContract campaignContract, String country) {
    this.campaignContract = campaignContract;
    this.country = country;
  }

  public List<Campaign> getActiveCampaings(String packageName) {
    return campaignContract.getActiveCampaings(packageName);
  }
}
