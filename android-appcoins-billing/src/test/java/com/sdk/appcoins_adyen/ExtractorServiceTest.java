package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.payasguest.OemIdExtractor;
import com.appcoins.sdk.billing.payasguest.OemIdExtractorFromExternalLib;
import com.appcoins.sdk.billing.payasguest.OemIdExtractorFromProperties;
import com.appcoins.sdk.billing.service.address.OemIdExtractorService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExtractorServiceTest {

  private OemIdExtractor oemIdExtractorFromExternalLib;
  private OemIdExtractor oemIdExtractorFromProperties;
  private List<OemIdExtractor> oemIdExtractorList;

  @Before public void setup() {
    this.oemIdExtractorFromExternalLib = mock(OemIdExtractorFromExternalLib.class);
    this.oemIdExtractorFromProperties = mock(OemIdExtractorFromProperties.class);
    this.oemIdExtractorList = new ArrayList<>();
    oemIdExtractorList.add(this.oemIdExtractorFromExternalLib);
    oemIdExtractorList.add(this.oemIdExtractorFromProperties);
  }

  @Test public void extractorServiceV2Test() {

    when(oemIdExtractorFromExternalLib.extract("com.appcoins.toolbox")).thenReturn(
        "toolbox_oemIdv2");
    when(oemIdExtractorFromProperties.extract("com.appcoins.toolbox")).thenReturn(
        "toolbox_oemIdv1");

    OemIdExtractorService oemIdExtractorService = new OemIdExtractorService(oemIdExtractorList);

    String oemIdResult = oemIdExtractorService.extractOemId("com.appcoins.toolbox");
    Assert.assertEquals(oemIdResult, "toolbox_oemIdv2");
  }

  @Test public void extractorServiceV1Test() {

    when(oemIdExtractorFromExternalLib.extract("com.appcoins.toolbox")).thenReturn("");
    when(oemIdExtractorFromProperties.extract("com.appcoins.toolbox")).thenReturn(
        "toolbox_oemIdv1");

    OemIdExtractorService oemIdExtractorService = new OemIdExtractorService(oemIdExtractorList);

    String oemIdResult = oemIdExtractorService.extractOemId("com.appcoins.toolbox");
    Assert.assertEquals(oemIdResult, "toolbox_oemIdv1");
  }

  @Test public void extractorServiceNullTest() {

    when(oemIdExtractorFromExternalLib.extract("com.appcoins.toolbox")).thenReturn(null);
    when(oemIdExtractorFromProperties.extract("com.appcoins.toolbox")).thenReturn(null);

    OemIdExtractorService oemIdExtractorService = new OemIdExtractorService(oemIdExtractorList);

    String oemIdResult = oemIdExtractorService.extractOemId("com.appcoins.toolbox");
    Assert.assertEquals(oemIdResult, "");
  }
}
