package com.appcoins.communication.sender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;

class MessageReceiver extends BroadcastReceiver {
  private MessageReceivedListener listener;

  MessageReceiver(Context context, String receiverUri)
      throws IntentFilter.MalformedMimeTypeException {
    IntentFilter filter = new IntentFilter(Intent.ACTION_VIEW, receiverUri);
    context.registerReceiver(this, filter);
  }

  public void setListener(MessageReceivedListener listener) {
    this.listener = listener;
  }

  @Override public void onReceive(Context context, Intent intent) {
    long messageId = intent.getLongExtra("MESSAGE_ID", -1);
    Parcelable returnValue = intent.getParcelableExtra("RETURN_VALUE");
    listener.onMessageReceived(messageId, returnValue);
  }

  interface MessageReceivedListener {
    void onMessageReceived(long messageId, Parcelable returnValue);
  }
}
