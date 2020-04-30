package com.sdk.appcoins_lifecycle.lifecycle.common;

import com.sdk.appcoins_lifecycle.lifecycle.LifecycleObserver;
import com.sdk.appcoins_lifecycle.lifecycle.LifecycleOwner;

interface FullLifecycleObserver extends LifecycleObserver {

  void onCreate(LifecycleOwner owner);

  void onStart(LifecycleOwner owner);

  void onResume(LifecycleOwner owner);

  void onPause(LifecycleOwner owner);

  void onStop(LifecycleOwner owner);

  void onDestroy(LifecycleOwner owner);
}
