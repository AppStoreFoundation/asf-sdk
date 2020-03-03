package com.appcoins.communication.requester;

import android.os.Parcelable;

interface MessageRequesterListener {
  void onMessageReceived(long requestCode, Parcelable returnValue);
}
