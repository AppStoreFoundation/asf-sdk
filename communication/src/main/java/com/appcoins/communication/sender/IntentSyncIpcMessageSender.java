package com.appcoins.communication.sender;

import android.os.Looper;
import android.os.Parcelable;
import com.appcoins.communication.SyncIpcMessageSender;

public class IntentSyncIpcMessageSender implements SyncIpcMessageSender {
  private final MessageSender messageSender;
  private final MessageResponseSynchronizer messageResponseSynchronizer;
  private final IdGenerator idGenerator;

  public IntentSyncIpcMessageSender(MessageSender messageSender,
      MessageResponseSynchronizer messageResponseSynchronizer, IdGenerator idGenerator) {
    this.messageSender = messageSender;
    this.messageResponseSynchronizer = messageResponseSynchronizer;
    this.idGenerator = idGenerator;
  }

  @Override public Parcelable sendMessage(int type, Parcelable arguments)
      throws MainThreadException, InterruptedException {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      throw new MainThreadException("sendMessage");
    }
    long messageId = idGenerator.generateId();
    messageSender.sendMessage(messageId, type, arguments);
    return messageResponseSynchronizer.waitMessage(messageId);
  }
}
