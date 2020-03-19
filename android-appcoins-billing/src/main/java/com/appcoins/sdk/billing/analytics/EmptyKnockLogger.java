package com.appcoins.sdk.billing.analytics;

import cm.aptoide.analytics.KnockEventLogger;

class EmptyKnockLogger implements KnockEventLogger {
  @Override public void log(String url) {

  }
}
