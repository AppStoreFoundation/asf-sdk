package com.asf.appcoins.sdk.ads.lifecycle;

/**
 * @deprecated Use {@code android.support.v7.app.AppCompatActivity}
 * which extends {@link LifecycleOwner}, so there are no use cases for this class.
 */
@SuppressWarnings({ "WeakerAccess", "unused" }) @Deprecated public interface LifecycleRegistryOwner
    extends LifecycleOwner {
  @Override LifecycleRegistry getLifecycle();
}