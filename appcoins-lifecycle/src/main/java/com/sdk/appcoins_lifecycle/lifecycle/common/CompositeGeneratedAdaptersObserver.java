package com.sdk.appcoins_lifecycle.lifecycle.common;

import com.sdk.appcoins_lifecycle.lifecycle.Lifecycle;
import com.sdk.appcoins_lifecycle.lifecycle.LifecycleEventObserver;
import com.sdk.appcoins_lifecycle.lifecycle.LifecycleOwner;

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