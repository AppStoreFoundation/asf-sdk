package com.appcoins.communication;

import android.os.Parcelable;

class IntentSyncIpcExecutor implements SyncIpcExecutor {

  public IntentSyncIpcExecutor() {
  }

  @Override public Parcelable sendMessage(int type, Parcelable arguments) {
    return new Person("José");
  }
}
