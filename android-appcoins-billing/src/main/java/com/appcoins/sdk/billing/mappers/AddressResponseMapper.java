package com.appcoins.sdk.billing.mappers;

import com.appcoins.sdk.billing.models.AddressModel;
import com.appcoins.sdk.billing.service.RequestResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.appcoins.sdk.billing.utils.ServiceUtils.isSuccess;

public class AddressResponseMapper {

  public AddressResponseMapper() {

  }

  public AddressModel map(RequestResponse requestResponse, String defaultAddress) {
    JSONObject jsonObject;
    AddressModel addressModel = new AddressModel(defaultAddress);
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    if (isSuccess(code) && response != null) {
      try {
        jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        JSONObject itemObject = jsonArray.optJSONObject(0);
        if (itemObject != null) {
          JSONObject userObject = itemObject.getJSONObject("user");
          String address = userObject.getString("wallet_address");
          addressModel = new AddressModel(address, false);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return addressModel;
  }

  public AddressModel mapDeveloper(RequestResponse requestResponse) {
    JSONObject jsonObject;
    AddressModel addressModel = new AddressModel("");
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    if (isSuccess(code) && response != null) {
      try {
        jsonObject = new JSONObject(response);
        JSONObject userObject = jsonObject.getJSONObject("data");
        String address = userObject.getString("address");
        addressModel = new AddressModel(address, false);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return addressModel;
  }
}
