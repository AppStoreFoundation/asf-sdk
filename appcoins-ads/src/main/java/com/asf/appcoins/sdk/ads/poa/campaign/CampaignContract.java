package com.asf.appcoins.sdk.ads.poa.campaign;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface CampaignContract {

  String getPackageNameOfCampaign(BigInteger bidId) throws IOException;

  List<BigInteger> getCampaignsByCountry(String countryId) throws IOException;

  List<String> getCountryList() throws IOException;

  List<BigInteger> getVercodesOfCampaign(BigInteger bidId) throws IOException;

  boolean getCampaignValidity(BigInteger bidId) throws IOException;

  boolean isCampaignValid(BigInteger bidId);
}
