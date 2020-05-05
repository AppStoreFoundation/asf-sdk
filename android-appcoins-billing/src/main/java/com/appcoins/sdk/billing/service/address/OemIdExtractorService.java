package com.appcoins.sdk.billing.service.address;

import com.appcoins.sdk.billing.payasguest.OemIdExtractor;

public class OemIdExtractorService {

  private final OemIdExtractor extractorV1;
  private final OemIdExtractor extractorV2;

  public OemIdExtractorService(OemIdExtractor extractorV1, OemIdExtractor extractorV2) {
    this.extractorV1 = extractorV1;
    this.extractorV2 = extractorV2;
  }

  String extractOemId(String packageName) {
    String oemId = extractorV2.extract(packageName);
    if (oemId != null || !oemId.isEmpty()) {
      return oemId;
    }
    return extractorV1.extract(packageName);
  }
}


