package com.appcoins.communication;

import android.os.Parcelable;
import com.appcoins.communication.requester.MainThreadException;

public interface SyncIpcMessageRequester {
  /**
   * @param methodId id of the method that should be used to process your data
   * @param arguments arguments for the corresponding method
   *
   * @return the data returned by the processor application
   *
   * @throws InterruptedException if the current thread is interrupted while waiting for the
   * response from processor application
   * @throws MainThreadException Since this is a blocker method and we use activities(which
   * default methods run on main thread) you can't use main thread to send messages, it would
   * cause a deadlock
   */
  Parcelable sendMessage(int methodId, Parcelable arguments)
      throws InterruptedException, MainThreadException;
}
