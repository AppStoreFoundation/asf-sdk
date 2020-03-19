package com.appcoins.sdk.billing.analytics;

import cm.aptoide.analytics.AnalyticsManager;
import com.appcoins.sdk.billing.service.BdsService;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsManagerProvider {

  private static AnalyticsManager analyticsManagerInstance = null;

  public static AnalyticsManager provideAnalyticsManager() {
    if (analyticsManagerInstance == null) {
      return new AnalyticsManager.Builder().addLogger(
          new BackendEventLogger(new BdsService("https://ws75.aptoide.com/api/7/", 30000)),
          provideBiEventList())//30000 should later be updated for static variable in BdsService
          //.addLogger(new RakamEventLogger(), rakamEventList)
          .setAnalyticsNormalizer(new KeysNormalizer())
          .setKnockLogger(new EmptyKnockLogger())
          .setDebugLogger(new DebugLogger())
          .build();
    } else {
      return analyticsManagerInstance;
    }
  }

  private static List<String> provideBiEventList() {
    List<String> list = new ArrayList<>();
    list.add(BillingAnalytics.PURCHASE_DETAILS);
    list.add(BillingAnalytics.PAYMENT_METHOD_DETAILS);
    list.add(BillingAnalytics.PAYMENT);
    return list;
  }
}
