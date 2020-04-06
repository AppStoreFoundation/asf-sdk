package com.asf.appcoins.sdk.ads.lifecycle.common;

import com.asf.appcoins.sdk.ads.lifecycle.Lifecycle;
import com.asf.appcoins.sdk.ads.lifecycle.LifecycleOwner;

/**
 * @hide
 */
public interface GeneratedAdapter {

  /**
   * Called when a state transition event happens.
   *
   * @param source The source of the event
   * @param event The event
   * @param onAny approveCall onAny handlers
   * @param logger if passed, used to track called methods and prevent calling the same method
   * twice
   */
  void callMethods(LifecycleOwner source, Lifecycle.Event event, boolean onAny,
      MethodCallsLogger logger);
}
