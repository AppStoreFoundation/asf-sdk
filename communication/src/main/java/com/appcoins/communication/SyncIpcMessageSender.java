package com.appcoins.communication;

import android.os.Parcelable;
import com.appcoins.communication.sender.MainThreadException;

public interface SyncIpcMessageSender {
  Parcelable sendMessage(int type, Parcelable arguments)
      throws InterruptedException, MainThreadException;
}
