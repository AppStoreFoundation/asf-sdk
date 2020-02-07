package com.appcoins.communication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;

class MessageReceiver extends BroadcastReceiver {

  private MessageReceivedListener listener;

  public MessageReceiver(Context context, String receiverUri)
      throws IntentFilter.MalformedMimeTypeException {
    context.registerReceiver(this, new IntentFilter(Intent.ACTION_VIEW, receiverUri));
  }

  public void setListener(MessageReceivedListener listener) {
    this.listener = listener;
  }

  @Override public void onReceive(Context context, Intent intent) {
    long messageId = intent.getLongExtra("MESSAGE_ID", -1);
    int methodId = intent.getIntExtra("TYPE", -1);
    Parcelable arguments = intent.getParcelableExtra("ARGUMENTS");
    listener.onReceived(messageId, methodId, arguments);
  }

  interface MessageReceivedListener {
    void onReceived(long messageId, int methodId, Parcelable arguments);
  }
}

