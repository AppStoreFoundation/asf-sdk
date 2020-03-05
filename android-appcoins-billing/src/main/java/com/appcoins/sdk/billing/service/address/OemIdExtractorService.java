package com.appcoins.sdk.billing.service.address;

import com.appcoins.sdk.billing.payasguest.IExtractOemId;

public class OemIdExtractorService {

  private final IExtractOemId extractorV1;

  public OemIdExtractorService(IExtractOemId extractorV1) {

    this.extractorV1 = extractorV1;
  }

  String extractOemId(String packageName) {
    return extractorV1.extract(packageName);
  }
}


