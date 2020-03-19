package com.appcoins.sdk.billing.analytics;

import cm.aptoide.analytics.AnalyticsManager;
import cm.aptoide.analytics.EventLogger;
import com.appcoins.sdk.billing.service.BdsService;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

class RakamEventLogger implements EventLogger {

  private BdsService rakamService;
  private String packageName;

  RakamEventLogger(BdsService rakamService, String packageName) {

    this.rakamService = rakamService;
    this.packageName = packageName;
  }

  @Override
  public void log(String eventName, Map<String, Object> data, AnalyticsManager.Action action,
      String context) {
    //TO Implement
  }

  @Override public void setup() {

  }

  private JSONObject mapToJsonObject(Map<String, Object> data) {
    JSONObject eventData = new JSONObject();

    for (Map.Entry<String, Object> entry : data.entrySet()) {
      try {
        eventData.put(entry.getKey(), entry.getValue()
            .toString());
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return eventData;
  }
}
