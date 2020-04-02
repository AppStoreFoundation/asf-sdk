package com.appcoins.sdk.billing.mappers;

import com.appcoins.sdk.billing.models.GamificationModel;
import com.appcoins.sdk.billing.service.RequestResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.appcoins.sdk.billing.utils.ServiceUtils.isSuccess;

public class GamificationMapper {

  public GamificationMapper() {
  }

  public GamificationModel mapToMaxBonus(RequestResponse requestResponse) {
    JSONObject jsonObject;
    GamificationModel gamificationModel = GamificationModel.createErrorGamificationModel();
    int code = requestResponse.getResponseCode();
    String response = requestResponse.getResponse();
    if (isSuccess(code) && response != null) {
      try {
        jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        JSONObject level = jsonArray.getJSONObject(jsonArray.length() - 1);
        int maxBonus = level.getInt("bonus");
        String status = jsonObject.getString("status");
        gamificationModel = new GamificationModel(maxBonus, status, false);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return gamificationModel;
  }
}
