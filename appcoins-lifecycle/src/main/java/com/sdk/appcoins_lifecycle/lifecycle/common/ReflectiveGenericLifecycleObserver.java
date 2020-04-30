package com.sdk.appcoins_lifecycle.lifecycle.common;

import com.sdk.appcoins_lifecycle.lifecycle.Lifecycle;
import com.sdk.appcoins_lifecycle.lifecycle.LifecycleEventObserver;
import com.sdk.appcoins_lifecycle.lifecycle.LifecycleOwner;

/**
 * An internal implementation of {@link LifecycleObserver} that relies on reflection.
 */
class ReflectiveGenericLifecycleObserver implements LifecycleEventObserver {
  private final Object mWrapped;
  private final ClassesInfoCache.CallbackInfo mInfo;

  ReflectiveGenericLifecycleObserver(Object wrapped) {
    mWrapped = wrapped;
    mInfo = ClassesInfoCache.sInstance.getInfo(mWrapped.getClass());
  }

  @Override public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
    mInfo.invokeCallbacks(source, event, mWrapped);
  }
}
