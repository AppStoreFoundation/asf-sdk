package com.appcoins.communication.requester;

import android.os.Looper;
import android.os.Parcelable;
import com.appcoins.communication.SyncIpcMessageRequester;

public class IntentSyncIpcMessageSender implements SyncIpcMessageRequester {
  private final MessageRequesterSender messageSender;
  private final MessageRequesterSynchronizer messageResponseSynchronizer;
  private final IdGenerator idGenerator;

  public IntentSyncIpcMessageSender(MessageRequesterSender messageSender,
      MessageRequesterSynchronizer messageResponseSynchronizer, IdGenerator idGenerator) {
    this.messageSender = messageSender;
    this.messageResponseSynchronizer = messageResponseSynchronizer;
    this.idGenerator = idGenerator;
  }

  @Override public Parcelable sendMessage(int methodId, Parcelable arguments)
      throws MainThreadException, InterruptedException {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      throw new MainThreadException("sendMessage");
    }
    long requestCode = idGenerator.generateRequestCode();
    messageSender.sendMessage(requestCode, methodId, arguments);
    return messageResponseSynchronizer.waitMessage(requestCode);
  }
}
