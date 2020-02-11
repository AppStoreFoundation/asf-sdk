package com.appcoins.communication.sender;

import android.os.Parcelable;

class MessageResponseSynchronizer {
  public MessageResponseSynchronizer() {
    StaticMessageResponseSynchronizer.init();
  }

  public Parcelable waitMessage(long requestCode) throws InterruptedException {
    return StaticMessageResponseSynchronizer.waitMessage(requestCode);
  }
}
