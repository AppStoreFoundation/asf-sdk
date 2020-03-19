package com.appcoins.sdk.billing.analytics;

import cm.aptoide.analytics.AnalyticsManager;
import com.appcoins.sdk.billing.helpers.WalletUtils;
import com.appcoins.sdk.billing.service.BdsService;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsManagerProvider {

  private static AnalyticsManager analyticsManagerInstance = null;

  public static AnalyticsManager provideAnalyticsManager() {
    if (analyticsManagerInstance == null) {
      int timeout = 30000; // should later be updated for static variable in BdsService
      String packageName = WalletUtils.getContext()
          .getPackageName();
      BdsService backendService = new BdsService("https://ws75.aptoide.com/api/7/", timeout);
      BdsService rakamService =
          new BdsService("https://rakam-api.aptoide.com/event/collect", timeout);

      BackendEventLogger backendEventLogger = new BackendEventLogger(backendService, packageName);
      RakamEventLogger rakamEventLogger = new RakamEventLogger(rakamService, packageName);

      return new AnalyticsManager.Builder().addLogger(backendEventLogger, provideBiEventList())
          .addLogger(rakamEventLogger, provideRakamEventList())
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

  private static List<String> provideRakamEventList() {
    List<String> list = new ArrayList<>();
    list.add(BillingAnalytics.RAKAM_PAYMENT_METHOD);
    list.add(BillingAnalytics.RAKAM_PAYMENT_CONFIRMATION);
    list.add(BillingAnalytics.RAKAM_PAYMENT_CONCLUSION);
    list.add(BillingAnalytics.RAKAM_PAYMENT_START);
    return list;
  }
}
