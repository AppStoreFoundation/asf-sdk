package com.sdk.appcoins_lifecycle.lifecycle.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @hide
 */
public class MethodCallsLogger {
  private Map<String, Integer> mCalledMethods = new HashMap<>();

  /**
   * @hide
   */
  public boolean approveCall(String name, int type) {
    Integer nullableMask = mCalledMethods.get(name);
    int mask = nullableMask != null ? nullableMask : 0;
    boolean wasCalled = (mask & type) != 0;
    mCalledMethods.put(name, mask | type);
    return !wasCalled;
  }
}