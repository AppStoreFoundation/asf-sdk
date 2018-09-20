package com.asf.appcoins.sdk.ads.poa.campaign;

import android.support.annotation.NonNull;
import io.reactivex.Single;
import java.math.BigInteger;
import java.util.List;

public class BdsCampaignService implements CampaignService {
  private final String packageName;
  private final int versionCode;
  private final CampaignRepository repository;
  private final CountryProvider countryProvider;

  public BdsCampaignService(String packageName, int versionCode, CampaignRepository repository,
      CountryProvider countryProvider) {
    this.packageName = packageName;
    this.versionCode = versionCode;
    this.repository = repository;
    this.countryProvider = countryProvider;
  }

  @Override public Single<Campaign> getCampaign() {
    return countryProvider.getCountryCode()
        .flatMap(countryCode -> repository.getCampaign(packageName, versionCode, countryCode))
        .map(campaignResponse -> mapCampaign(campaignResponse.getResult()));
  }

  @NonNull private Campaign mapCampaign(List<CampaignResponse.CampaignEntry> campaignResponse) {
    if (campaignResponse.isEmpty()) {
      return Campaign.Empty();
    }
    return new Campaign(BigInteger.valueOf(campaignResponse.get(0)
        .getBidId()), packageName);
  }
}
