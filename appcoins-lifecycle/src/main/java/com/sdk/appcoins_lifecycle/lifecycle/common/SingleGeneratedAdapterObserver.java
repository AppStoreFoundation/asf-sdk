package com.sdk.appcoins_lifecycle.lifecycle.common;

import com.sdk.appcoins_lifecycle.lifecycle.Lifecycle;
import com.sdk.appcoins_lifecycle.lifecycle.LifecycleEventObserver;
import com.sdk.appcoins_lifecycle.lifecycle.LifecycleOwner;

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