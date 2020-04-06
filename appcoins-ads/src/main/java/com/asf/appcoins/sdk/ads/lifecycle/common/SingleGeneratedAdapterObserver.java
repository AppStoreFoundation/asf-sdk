package com.asf.appcoins.sdk.ads.lifecycle.common;

import com.asf.appcoins.sdk.ads.lifecycle.Lifecycle;
import com.asf.appcoins.sdk.ads.lifecycle.LifecycleEventObserver;
import com.asf.appcoins.sdk.ads.lifecycle.LifecycleOwner;

class SingleGeneratedAdapterObserver implements LifecycleEventObserver {

  private final GeneratedAdapter mGeneratedAdapter;

  SingleGeneratedAdapterObserver(GeneratedAdapter generatedAdapter) {
    mGeneratedAdapter = generatedAdapter;
  }

  @Override public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
    mGeneratedAdapter.callMethods(source, event, false, null);
    mGeneratedAdapter.callMethods(source, event, true, null);
  }
}