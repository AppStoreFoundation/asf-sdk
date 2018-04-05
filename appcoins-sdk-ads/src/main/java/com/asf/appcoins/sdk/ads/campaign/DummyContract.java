package com.asf.appcoins.sdk.ads.campaign;

import java.util.LinkedList;
import java.util.List;

class DummyContract implements CampaignContract {

  @Override public List<Campaign> getActiveCampaings(String packageName) {
    List<Campaign> campaigns = new LinkedList<>();

    campaigns.add(new Campaign(0, 1, "pt"));
    campaigns.add(new Campaign(1, 2, "uk"));
    campaigns.add(new Campaign(2, 3, "de"));

    return campaigns;
  }
}
