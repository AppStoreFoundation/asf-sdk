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
      BdsService rakamService =
          new BdsService("https://rakam-api.aptoide.com/event/collect", timeout);
      WalletAddressProvider walletAddressProvider =
          WalletAddressProvider.provideWalletAddressProvider();
      RakamEventLogger rakamEventLogger =
          new RakamEventLogger(rakamService, walletAddressProvider, WalletUtils.context);

      analyticsManagerInstance =
          new AnalyticsManager.Builder().addLogger(rakamEventLogger, provideRakamEventList())
              .setAnalyticsNormalizer(new KeysNormalizer())
              .setKnockLogger(new EmptyKnockLogger())
              .setDebugLogger(new DebugLogger())
              .build();
    }
    return analyticsManagerInstance;
  }

  private static List<String> provideRakamEventList() {
    List<String> list = new ArrayList<>();
    list.add(BillingAnalytics.PAYMENT_METHOD);
    list.add(BillingAnalytics.PAYMENT_CONFIRMATION);
    list.add(BillingAnalytics.PAYMENT_CONCLUSION);
    list.add(BillingAnalytics.PAYMENT_START);
    return list;
  }
}
