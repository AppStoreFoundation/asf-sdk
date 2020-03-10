package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.mappers.AddressResponseMapper;
import com.appcoins.sdk.billing.models.AddressModel;
import com.appcoins.sdk.billing.service.RequestResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AddressResponseMapperTest {

  private AddressResponseMapper addressResponseMapper;

  @Before public void setup() {
    addressResponseMapper = new AddressResponseMapper();
  }

  @Test public void addressResponseSuccessTest() {
    String response = "{\n"
        + "\t\"next\": null,\n"
        + "\t\"items\": [{\"user\": {\"wallet_address\":\"0x123\"} }]\n"
        + "}";
    AddressModel addressModel =
        addressResponseMapper.map(new RequestResponse(200, response, null), "0x");
    Assert.assertEquals(addressModel.getAddress(), "0x123");
  }

  @Test public void addressResponseEmptyTest() {
    String response = "{\n" + "\t\"next\": null,\n" + "\t\"items\": []\n" + "}";
    AddressModel addressModel =
        addressResponseMapper.map(new RequestResponse(200, response, null), "0x");
    Assert.assertEquals(addressModel.getAddress(), "0x");
  }

  @Test public void developerAddressResponseSuccessTest() {
    String response = "{\"data\": {\n" + "\t\t\"address\": \"0x123\"\n" + "\t}}";
    AddressModel addressModel =
        addressResponseMapper.mapDeveloper(new RequestResponse(200, response, null));
    Assert.assertEquals(addressModel.getAddress(), "0x123");
  }
}
