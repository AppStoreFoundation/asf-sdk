package com.asf.appcoins.sdk.ads.lifecycle;

/**
 * A class that has an Android lifecycle. These events can be used by custom components to
 * handle lifecycle changes without implementing any code inside the Activity or the Fragment.
 *
 * @see Lifecycle
 */
@SuppressWarnings({ "WeakerAccess", "unused" }) public interface LifecycleOwner {
  /**
   * Returns the Lifecycle of the provider.
   *
   * @return The lifecycle of the provider.
   */
  Lifecycle getLifecycle();
}
