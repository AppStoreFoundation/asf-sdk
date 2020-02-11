package com.appcoins.communication.sender;

import android.os.Parcelable;

class MessageResponseSynchronizer {
  public MessageResponseSynchronizer() {
    StaticMessageResponseSynchronizer.init();
  }

  public Parcelable waitMessage(long messageId) throws InterruptedException {
    return StaticMessageResponseSynchronizer.waitMessage(messageId);
  }
}
