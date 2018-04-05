package com.asf.appcoins.sdk.ads.campaign;

public class Campaign {

  private final long id;
  private final long vercode;
  private final String country;

  public Campaign(long id, long vercode, String country) {
    this.id = id;
    this.vercode = vercode;
    this.country = country;
  }

  public long getId() {
    return id;
  }

  public long getVercode() {
    return vercode;
  }

  public String getCountry() {
    return country;
  }
}
