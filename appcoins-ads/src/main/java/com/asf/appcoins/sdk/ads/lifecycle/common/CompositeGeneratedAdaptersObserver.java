package com.asf.appcoins.sdk.ads.lifecycle.common;

import com.asf.appcoins.sdk.ads.lifecycle.Lifecycle;
import com.asf.appcoins.sdk.ads.lifecycle.LifecycleEventObserver;
import com.asf.appcoins.sdk.ads.lifecycle.LifecycleOwner;

class CompositeGeneratedAdaptersObserver implements LifecycleEventObserver {

  private final GeneratedAdapter[] mGeneratedAdapters;

  CompositeGeneratedAdaptersObserver(GeneratedAdapter[] generatedAdapters) {
    mGeneratedAdapters = generatedAdapters;
  }

  @Override public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
    MethodCallsLogger logger = new MethodCallsLogger();
    for (GeneratedAdapter mGenerated : mGeneratedAdapters) {
      mGenerated.callMethods(source, event, false, logger);
    }
    for (GeneratedAdapter mGenerated : mGeneratedAdapters) {
      mGenerated.callMethods(source, event, true, logger);
    }
  }
}