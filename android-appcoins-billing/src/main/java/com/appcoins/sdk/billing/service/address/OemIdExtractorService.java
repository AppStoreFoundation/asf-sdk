package com.appcoins.sdk.billing.service.address;

import com.appcoins.sdk.billing.payasguest.OemIdExtractor;

public class OemIdExtractorService {

  private final OemIdExtractor extractorV1;

  public OemIdExtractorService(OemIdExtractor extractorV1) {

    this.extractorV1 = extractorV1;
  }

  String extractOemId(String packageName) {
    return extractorV1.extract(packageName);
  }
}


