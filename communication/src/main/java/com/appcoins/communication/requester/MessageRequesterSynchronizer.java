package com.appcoins.communication.requester;

import android.os.Parcelable;

class MessageRequesterSynchronizer {
  public MessageRequesterSynchronizer() {
    StaticMessageResponseSynchronizer.init();
  }

  public Parcelable waitMessage(long requestCode, int timeout) throws InterruptedException {
    return StaticMessageResponseSynchronizer.waitMessage(requestCode, timeout);
  }
}
