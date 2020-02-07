package com.appcoins.communication.sender;

import android.os.Parcelable;
import android.util.Log;
import java.util.Map;

class MessageResponseSynchronizer {

  private final Map<Long, Object> blockingObjects;
  private final Map<Long, Parcelable> responses;
  private static final String TAG = MessageResponseSynchronizer.class.getSimpleName();

  MessageResponseSynchronizer(MessageReceiver messageReceiver,
      final Map<Long, Object> blockingObjects, final Map<Long, Parcelable> responses) {
    this.blockingObjects = blockingObjects;
    this.responses = responses;
    messageReceiver.setListener(new MessageReceiver.MessageReceivedListener() {
      @Override public void onMessageReceived(long messageId, Parcelable returnValue) {
        responses.put(messageId, returnValue);
        Object blockingObject = blockingObjects.get(messageId);
        if (blockingObject == null) {
          Log.w(TAG, "there is no request for message id: " + messageId);
          return;
        }
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (blockingObject) {
          blockingObject.notifyAll();
        }
      }
    });
  }

  public Parcelable waitMessage(long messageId) throws InterruptedException {
    if (!responses.containsKey(messageId)) {
      Object blockingObject = new Object();
      blockingObjects.put(messageId, blockingObject);
      //noinspection SynchronizationOnLocalVariableOrMethodParameter
      synchronized (blockingObject) {
        blockingObject.wait();
      }
    }
    return responses.get(messageId);
  }
}
