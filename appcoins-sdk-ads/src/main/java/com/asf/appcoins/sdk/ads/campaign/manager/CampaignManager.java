package com.asf.appcoins.sdk.ads.campaign.manager;

import com.asf.appcoins.sdk.ads.campaign.Campaign;
import com.asf.appcoins.sdk.ads.campaign.contract.CampaignContract;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.Web3j;

public class CampaignManager {

  private final CampaignContract campaignContract;
  private final String country;

  public CampaignManager(Web3j web3j, Address contractAddress) {
    this(new CampaignContractImpl(web3j, contractAddress), "PT");
  }

  public CampaignManager(CampaignContract campaignContract, String country) {
    this.campaignContract = campaignContract;
    this.country = country;
  }

  public List<Campaign> getActiveCampaings(String packageName, BigInteger vercode)
      throws IOException {
    List<BigInteger> campaignsIdsByCountry = campaignContract.getCampaignsByCountry(country);
    List<Campaign> campaings = new LinkedList<>();

    for (BigInteger bidId : campaignsIdsByCountry) {
      String campaignPackageName = campaignContract.getPackageNameOfCampaign(bidId);
      List<BigInteger> vercodes = campaignContract.getVercodesOfCampaign(bidId);

      if (campaignPackageName.equals(packageName) && vercodes.contains(vercode)) {
        campaings.add(new Campaign(bidId, vercodes, country));
      }
    }

    return campaings;
  }
}