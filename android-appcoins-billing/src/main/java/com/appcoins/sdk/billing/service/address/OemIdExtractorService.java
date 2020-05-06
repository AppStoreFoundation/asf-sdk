package com.appcoins.sdk.billing.service.address;

import com.appcoins.sdk.billing.BuildConfig;
import com.appcoins.sdk.billing.payasguest.OemIdExtractor;
import java.util.List;

public class OemIdExtractorService {

  private final List<OemIdExtractor> extractorList;

  public OemIdExtractorService(List<OemIdExtractor> oemIdExtractorsList) {
    this.extractorList = oemIdExtractorsList;
  }

  public String extractOemId(String packageName) {
    for (OemIdExtractor oemIdExtractor : extractorList) {
      String oemId = oemIdExtractor.extract(packageName);
      if (oemId != null && !oemId.isEmpty()) {
        return oemId;
      }
    }
    return null;
  }
}


