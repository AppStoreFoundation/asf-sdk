package com.asf.appcoins.sdk.ads.lifecycle;

/**
 * Class that can receive any lifecycle change and dispatch it to the receiver.
 * <p>
 * If a class implements both this interface and
 * {@link androidx.lifecycle.DefaultLifecycleObserver}, then
 * methods of {@code DefaultLifecycleObserver} will be called first, and then followed by the call
 * of {@link LifecycleEventObserver#onStateChanged(LifecycleOwner, Lifecycle.Event)}
 * <p>
 * If a class implements this interface and in the same time uses {@link OnLifecycleEvent}, then
 * annotations will be ignored.
 */
public interface LifecycleEventObserver extends LifecycleObserver {
  /**
   * Called when a state transition event happens.
   *
   * @param source The source of the event
   * @param event The event
   */
  void onStateChanged(LifecycleOwner source, Lifecycle.Event event);
}
