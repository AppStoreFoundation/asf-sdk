package com.appcoins.sdk.billing.mappers;

import com.appcoins.sdk.billing.service.RequestResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.appcoins.sdk.billing.utils.ServiceUtils.isSuccess;

public class GamificationMapper {
  public GamificationMapper() {
  }

  public int mapToMaxBonus(RequestResponse requestResponse) {
    JSONObject jsonObject;
    int maxBonus = -1;
    int code = requestResponse.getResponseCode();
    String response = requestResponse.getResponse();

    if (isSuccess(code) && response != null) {
      try {
        jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        JSONObject level = jsonArray.getJSONObject(jsonArray.length() - 1);
        double maxBonusEntity = level.getInt("bonus");
        maxBonus = (int) maxBonusEntity;
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return maxBonus;
  }
}
