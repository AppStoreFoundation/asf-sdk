package com.appcoins.sdk.billing.payasguest;

public class OemIdExtractorService {

  private final IExtractOemId extractorV1;
  private final IExtractOemId extractorV2;

  public OemIdExtractorService(IExtractOemId extractorV1, IExtractOemId extractorV2) {

    this.extractorV1 = extractorV1;
    this.extractorV2 = extractorV2;
  }

  public String extractOemId(String packageName) {
    String oemId = extractorV2.extract(packageName);
    if (!oemId.equals("")) {
      return oemId;
    } else {
      return extractorV1.extract(packageName);
    }
  }
}


