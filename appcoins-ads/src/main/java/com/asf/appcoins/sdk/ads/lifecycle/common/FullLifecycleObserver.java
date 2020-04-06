package com.asf.appcoins.sdk.ads.lifecycle.common;

import com.asf.appcoins.sdk.ads.lifecycle.LifecycleObserver;
import com.asf.appcoins.sdk.ads.lifecycle.LifecycleOwner;

interface FullLifecycleObserver extends LifecycleObserver {

  void onCreate(LifecycleOwner owner);

  void onStart(LifecycleOwner owner);

  void onResume(LifecycleOwner owner);

  void onPause(LifecycleOwner owner);

  void onStop(LifecycleOwner owner);

  void onDestroy(LifecycleOwner owner);
}
