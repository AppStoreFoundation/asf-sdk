package com.appcoins.communication.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

class MessageSender {
  private final Context context;
  private final String senderUri;

  public MessageSender(Context context, String senderUri) {
    this.context = context;
    this.senderUri = senderUri;
  }

  public void sendMessage(long messageId, Parcelable response) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(senderUri));
    intent.putExtra("MESSAGE_ID", messageId);
    intent.putExtra("RETURN_VALUE", response);
    context.sendBroadcast(intent);
  }
}
