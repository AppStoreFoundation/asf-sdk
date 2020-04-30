package com.sdk.appcoins_lifecycle.lifecycle.common;

import com.sdk.appcoins_lifecycle.lifecycle.Lifecycle;
import com.sdk.appcoins_lifecycle.lifecycle.LifecycleEventObserver;
import com.sdk.appcoins_lifecycle.lifecycle.LifecycleOwner;

class FullLifecycleObserverAdapter implements LifecycleEventObserver {

  private final FullLifecycleObserver mFullLifecycleObserver;
  private final LifecycleEventObserver mLifecycleEventObserver;

  FullLifecycleObserverAdapter(FullLifecycleObserver fullLifecycleObserver,
      LifecycleEventObserver lifecycleEventObserver) {
    mFullLifecycleObserver = fullLifecycleObserver;
    mLifecycleEventObserver = lifecycleEventObserver;
  }

  @Override public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
    switch (event) {
      case ON_CREATE:
        mFullLifecycleObserver.onCreate(source);
        break;
      case ON_START:
        mFullLifecycleObserver.onStart(source);
        break;
      case ON_RESUME:
        mFullLifecycleObserver.onResume(source);
        break;
      case ON_PAUSE:
        mFullLifecycleObserver.onPause(source);
        break;
      case ON_STOP:
        mFullLifecycleObserver.onStop(source);
        break;
      case ON_DESTROY:
        mFullLifecycleObserver.onDestroy(source);
        break;
      case ON_ANY:
        throw new IllegalArgumentException("ON_ANY must not been send by anybody");
    }
    if (mLifecycleEventObserver != null) {
      mLifecycleEventObserver.onStateChanged(source, event);
    }
  }
}
