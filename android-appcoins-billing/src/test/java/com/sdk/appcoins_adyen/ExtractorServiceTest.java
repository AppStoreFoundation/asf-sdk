package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.payasguest.OemIdExtractor;
import com.appcoins.sdk.billing.payasguest.OemIdExtractorV1;
import com.appcoins.sdk.billing.payasguest.OemIdExtractorV2;
import com.appcoins.sdk.billing.service.address.OemIdExtractorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExtractorServiceTest {

  private OemIdExtractor extractorV2;
  private OemIdExtractor extractorV1;

  @Before public void setup() {
    extractorV2 = mock(OemIdExtractorV2.class);
    extractorV1 = mock(OemIdExtractorV1.class);
  }

  @Test public void extractorServiceV2Test() {

    when(extractorV2.extract("com.appcoins.toolbox")).thenReturn("toolbox_oemIdv2");
    when(extractorV1.extract("com.appcoins.toolbox")).thenReturn("toolbox_oemIdv1");

    OemIdExtractorService oemIdExtractorService =
        new OemIdExtractorService(extractorV1, extractorV2);

    String oemIdResult = oemIdExtractorService.extractOemId("com.appcoins.toolbox");
    Assert.assertEquals(oemIdResult, "toolbox_oemIdv2");
  }

  @Test public void extractorServiceV1Test() {

    when(extractorV2.extract("com.appcoins.toolbox")).thenReturn("");
    when(extractorV1.extract("com.appcoins.toolbox")).thenReturn("toolbox_oemIdv1");

    OemIdExtractorService oemIdExtractorService =
        new OemIdExtractorService(extractorV1, extractorV2);

    String oemIdResult = oemIdExtractorService.extractOemId("com.appcoins.toolbox");
    Assert.assertEquals(oemIdResult, "toolbox_oemIdv1");
  }

  @Test public void extractorServiceNullTest() {

    when(extractorV2.extract("com.appcoins.toolbox")).thenReturn(null);
    when(extractorV1.extract("com.appcoins.toolbox")).thenReturn(null);

    OemIdExtractorService oemIdExtractorService =
        new OemIdExtractorService(extractorV1, extractorV2);

    String oemIdResult = oemIdExtractorService.extractOemId("com.appcoins.toolbox");
    Assert.assertEquals(oemIdResult, null);
  }
}
