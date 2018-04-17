package com.asf.appcoins.sdk.ads.poa.campaign;

import java.math.BigInteger;
import java.util.List;

public class Campaign {

  private final BigInteger id;
  private final List<BigInteger> vercodes;
  /**
   * An uppercase ISO 3166-1 3-letter code representing the current country.
   */
  private final String country;

  public Campaign(BigInteger id, List<BigInteger> vercodes, String country) {
    this.id = id;
    this.vercodes = vercodes;
    this.country = country;
  }

  public BigInteger getId() {
    return id;
  }

  public List<BigInteger> getVercodes() {
    return vercodes;
  }

  public String getCountry() {
    return country;
  }
}
