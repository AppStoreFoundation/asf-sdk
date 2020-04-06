package com.sdk.appcoins_lifecycle.lifecycle.common;

import com.sdk.appcoins_lifecycle.lifecycle.Lifecycle;
import com.sdk.appcoins_lifecycle.lifecycle.LifecycleOwner;

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
