package com.asf.appcoins.sdk.ads.campaign.manager;

import com.asf.appcoins.sdk.ads.campaign.Campaign;
import com.asf.appcoins.sdk.ads.campaign.contract.CampaignContract;
import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import org.web3j.abi.datatypes.Address;

public class CampaignManager {

  private final CampaignContract campaignContract;
  private final String country;

  public CampaignManager(AsfWeb3j asfWeb3j, Address contractAddress) {
    this(new CampaignContractImpl(asfWeb3j, contractAddress), "PT");
  }

  public CampaignManager(CampaignContract campaignContract, String country) {
    this.campaignContract = campaignContract;
    this.country = country;
  }

  public List<Campaign> getActiveCampaigns(String packageName, BigInteger vercode)
      throws IOException {
    List<BigInteger> campaignsIdsByCountry = campaignContract.getCampaignsByCountry(country);
    List<Campaign> campaign = new LinkedList<>();

    for (BigInteger bidId : campaignsIdsByCountry) {
      String campaignPackageName = campaignContract.getPackageNameOfCampaign(bidId);
      List<BigInteger> vercodes = campaignContract.getVercodesOfCampaign(bidId);

      if (campaignPackageName.equals(packageName) && vercodes.contains(vercode)) {
        campaign.add(new Campaign(bidId, vercodes, country));
      }
    }

    return campaign;
  }
}