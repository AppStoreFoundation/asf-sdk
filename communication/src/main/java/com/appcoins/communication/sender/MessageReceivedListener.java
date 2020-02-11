package com.appcoins.communication.sender;

import android.os.Parcelable;

interface MessageReceivedListener {
  void onMessageReceived(long requestCode, Parcelable returnValue);
}
