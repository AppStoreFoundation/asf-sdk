package com.asf.appcoins.sdk.ads.lifecycle.common;

import com.asf.appcoins.sdk.ads.lifecycle.Lifecycle;
import com.asf.appcoins.sdk.ads.lifecycle.LifecycleEventObserver;
import com.asf.appcoins.sdk.ads.lifecycle.LifecycleOwner;

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
