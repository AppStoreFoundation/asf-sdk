package com.appcoins.communication;

import android.os.Parcelable;

public interface SyncIpcMessageSender {
  Parcelable sendMessage(int type, Parcelable arguments) throws InterruptedException;
}
