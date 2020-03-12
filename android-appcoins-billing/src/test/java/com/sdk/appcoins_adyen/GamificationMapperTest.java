package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.mappers.GamificationMapper;
import com.appcoins.sdk.billing.models.GamificationModel;
import com.appcoins.sdk.billing.service.RequestResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GamificationMapperTest {

  private GamificationMapper gamificationMapper;

  @Before public void setup() {
    gamificationMapper = new GamificationMapper();
  }

  @Test public void gamificationResponseSuccessTest() {
    String response = "{\n"
        + "\t\"result\": [{\n"
        + "\t\t\"amount\": 0.0,\n"
        + "\t\t\"bonus\": 10.0,\n"
        + "\t\t\"level\": 0\n"
        + "\t}, {\n"
        + "\t\t\"amount\": 50.0,\n"
        + "\t\t\"bonus\": 15.0,\n"
        + "\t\t\"level\": 1\n"
        + "\t}, {\n"
        + "\t\t\"amount\": 100.0,\n"
        + "\t\t\"bonus\": 20.0,\n"
        + "\t\t\"level\": 2\n"
        + "\t}, {\n"
        + "\t\t\"amount\": 1000.0,\n"
        + "\t\t\"bonus\": 25.0,\n"
        + "\t\t\"level\": 3\n"
        + "\t}, {\n"
        + "\t\t\"amount\": 5000.0,\n"
        + "\t\t\"bonus\": 30.0,\n"
        + "\t\t\"level\": 4\n"
        + "\t}],\n"
        + "\t\"status\": \"ACTIVE\"\n"
        + "}";
    GamificationModel gamificationModel =
        gamificationMapper.mapToMaxBonus(new RequestResponse(200, response, null));
    Assert.assertEquals(30, gamificationModel.getMaxBonus());
    Assert.assertEquals("ACTIVE", gamificationModel.getStatus());
  }
}
