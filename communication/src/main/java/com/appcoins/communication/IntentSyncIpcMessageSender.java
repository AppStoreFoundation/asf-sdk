package com.appcoins.communication;

import android.os.Parcelable;

class IntentSyncIpcMessageSender implements SyncIpcMessageSender {
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
      throws InterruptedException {
    long messageId = idGenerator.generateId();
    messageSender.sendMessage(messageId, type, arguments);
    return (Person) messageResponseSynchronizer.waitMessage(messageId);
  }
}
