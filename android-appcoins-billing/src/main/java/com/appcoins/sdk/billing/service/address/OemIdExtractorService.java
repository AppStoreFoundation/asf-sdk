package com.appcoins.sdk.billing.service.address;

import com.appcoins.sdk.billing.payasguest.OemIdExtractor;

public class OemIdExtractorService {

  private final OemIdExtractor extractor;

  public OemIdExtractorService(OemIdExtractor extractor) {
    this.extractor = extractor;
  }

  String extractOemId(String packageName) {
    return extractor.extract(packageName);
  }
}


