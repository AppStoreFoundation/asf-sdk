package com.appcoins.sdk.billing.helpers;

import org.junit.Assert;
import org.junit.Test;

public class PayloadHelperTest {

    @Test
    public void buildIntentPayloadWithEmptyDeveloperPayload() {
        String developerPayload = "";
        String intentPayload = PayloadHelper.buildIntentPayload(null,
                developerPayload, null);
        Assert.assertEquals("appcoins://appcoins.io", intentPayload);
    }

    @Test
    public void buildIntentPayloadWithNullDeveloperPayload() {
        String intentPayload = PayloadHelper.buildIntentPayload(null,
                null, null);
        Assert.assertEquals("appcoins://appcoins.io", intentPayload);
    }

    @Test
    public void buildIntentPayloadSuccess() {
        String developerPayload = "suite1";
        String intentPayload = PayloadHelper.buildIntentPayload(null,
                developerPayload, null);
        Assert.assertEquals("appcoins://appcoins.io?payload=suite1", intentPayload);
    }
}
