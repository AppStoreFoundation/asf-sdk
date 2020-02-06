package com.appcoins.communication;

import android.os.Parcelable;

interface SyncIpcExecutor {
  Parcelable sendMessage(int type, Parcelable arguments) throws InterruptedException;
}
