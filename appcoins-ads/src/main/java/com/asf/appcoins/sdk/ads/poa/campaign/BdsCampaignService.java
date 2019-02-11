package com.asf.appcoins.sdk.ads.poa.campaign;

public class BdsCampaignService implements CampaignService {
  /*
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

  */

  @Override public Campaign getCampaign() {
    return null;
    /*
    return countryProvider.getCountryCode()
        .flatMap(countryCode -> repository.getCampaign(packageName, versionCode, countryCode,
            CampaignRepository.CampaignType.BDS))
        .map(campaignResponse -> mapCampaign(campaignResponse.getResult()));*/
  }
  /*

  //TODO -mudar -
  @Override public Single<Campaign> getCampaign() {
    /*return countryProvider.getCountryCode()
        .flatMap(countryCode -> repository.getCampaign(packageName, versionCode, countryCode,
            CampaignRepository.CampaignType.BDS))
        .map(campaignResponse -> mapCampaign(campaignResponse.getResult()));
    return null;
  }
  */

  /*@NonNull private Campaign mapCampaign(List<CampaignResponse.CampaignEntry> campaignResponse) {
    return new Campaign(BigInteger.valueOf(campaignResponse.get(0)
        .getBidId()), packageName);

  }
  */


}
