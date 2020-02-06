package com.appcoins.sdk.billing.service.wallet;

import com.appcoins.sdk.billing.service.RequestResponse;
import org.json.JSONException;
import org.json.JSONObject;

import static com.appcoins.sdk.billing.utils.ServiceUtils.isSuccess;

public class WalletGenerationMapper {

  public WalletGenerationMapper() {

  }

  WalletGenerationResponse mapWalletGenerationResponse(RequestResponse requestResponse) {
    JSONObject jsonObject;
    WalletGenerationResponse walletGenerationResponse = new WalletGenerationResponse();
    String response = requestResponse.getResponse();
    String walletAddress;
    String ewt;
    int code = requestResponse.getResponseCode();
    if (isSuccess(code) && response != null) {
      try {
        jsonObject = new JSONObject(response);
        walletAddress = jsonObject.getString("address");
        ewt = jsonObject.getString("ewt");
        walletGenerationResponse = new WalletGenerationResponse(walletAddress, ewt, false);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return walletGenerationResponse;
  }
}
