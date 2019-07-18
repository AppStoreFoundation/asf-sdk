package com.asf.appcoins.sdk.ads.poa.campaign;

import java.math.BigInteger;

public class Campaign {

  public static final int INVALID_CAMPAIGN = -1;
  private final BigInteger id;
  private final String packageName;

  public Campaign(BigInteger id, String packageName) {
    this.id = id;
    this.packageName = packageName;
  }

  public static Campaign Empty() {
    return new Campaign(BigInteger.valueOf(-1), "");
  }

  public String getPackageName() {
    return packageName;
  }

  public boolean hasCampaign() {
    return this.id.compareTo(BigInteger.valueOf(INVALID_CAMPAIGN)) != 0;
  }

  public BigInteger getId() {
    return id;
  }
}
