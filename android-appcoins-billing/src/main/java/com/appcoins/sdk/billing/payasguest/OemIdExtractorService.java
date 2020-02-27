package com.appcoins.sdk.billing.payasguest;

public class OemIdExtractorService {

  private final IExtractOemId extractorV1;

  public OemIdExtractorService(IExtractOemId extractorV1) {

    this.extractorV1 = extractorV1;
  }

  public String extractOemId(String packageName) {
    return extractorV1.extract(packageName);
  }
}


