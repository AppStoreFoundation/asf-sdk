package com.appcoins.sdk.billing.analytics;

import cm.aptoide.analytics.AnalyticsManager;
import cm.aptoide.analytics.EventLogger;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.service.BdsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BackendEventLogger implements EventLogger {

  private BdsService bdsService;
  private String packageName;

  BackendEventLogger(BdsService bdsService, String packageName) {
    this.bdsService = bdsService;
    this.packageName = packageName;
  }

  @Override
  public void log(String eventName, Map<String, Object> data, AnalyticsManager.Action action,
      String context) {
    List<String> path = new ArrayList<>();
    path.add("action=" + action);
    path.add("context=AppCoinsGuestSDK");
    path.add("name=" + eventName);

    Map<String, Object> body = new HashMap<>();
    body.put("data", data);
    body.put("aptoide_vercode", BuildConfig.VERSION_CODE);
    body.put("aptoide_package", packageName);
    bdsService.makeRequest("user/addEvent", "POST", path, new HashMap<String, String>(),
        new HashMap<String, String>(), body, null);
  }

  @Override public void setup() {

  }
}
