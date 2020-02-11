package com.appcoins.communication.requester;

import android.os.Parcelable;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

class StaticMessageResponseSynchronizer {

  static private final Map<Long, Object> blockingObjects = new HashMap<>();
  static private final Map<Long, Parcelable> responses = new HashMap<>();
  static private final String TAG = StaticMessageResponseSynchronizer.class.getSimpleName();
  static private MessageRequesterListener messageReceivedListener;

  private StaticMessageResponseSynchronizer() {
  }

  static void init() {
    messageReceivedListener = new MessageRequesterListener() {
      @Override public void onMessageReceived(long requestCode, Parcelable returnValue) {
        responses.put(requestCode, returnValue);
        Object blockingObject = blockingObjects.get(requestCode);
        if (blockingObject == null) {
          Log.w(TAG, "there is no request for message id: " + requestCode);
          return;
        }
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (blockingObject) {
          blockingObject.notifyAll();
        }
      }
    };
  }

  /**
   * Block the thread until a response value is returned.
   * {@link StaticMessageResponseSynchronizer} should be initialized before calling waitMessage
   * method. See {@link StaticMessageResponseSynchronizer#init()}
   *
   * @param requestCode id of the message to wait for
   *
   * @return the response from the target application
   *
   * @throws InterruptedException if the waiter thread is interrupted
   * @throws IllegalStateException if {@link StaticMessageResponseSynchronizer#init()} not called
   * before calling waitMessage method
   * @see StaticMessageResponseSynchronizer#init()
   */
  public static Parcelable waitMessage(long requestCode)
      throws InterruptedException, IllegalStateException {
    checkIfInitialized();
    if (!responses.containsKey(requestCode)) {
      Object blockingObject = new Object();
      blockingObjects.put(requestCode, blockingObject);
      //noinspection SynchronizationOnLocalVariableOrMethodParameter
      synchronized (blockingObject) {
        blockingObject.wait();
      }
    }
    return responses.get(requestCode);
  }

  private static void checkIfInitialized() throws IllegalStateException {
    if (messageReceivedListener == null) {
      throw new IllegalStateException(
          "StaticMessageResponseSynchronizer class must be initialized before being used.");
    }
  }

  /**
   * @throws IllegalStateException if {@link StaticMessageResponseSynchronizer#init()} not called
   * * before calling waitMessage method
   * * @see StaticMessageResponseSynchronizer#init()
   */
  public static MessageRequesterListener getMessageListener() throws IllegalStateException {
    checkIfInitialized();
    return messageReceivedListener;
  }
}
