package com.appcoins.communication.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

public class ReturnSender {
  private final Context context;
  private final String senderUri;

  public ReturnSender(Context context, String senderUri) {
    this.context = context;
    this.senderUri = senderUri;
  }

  public void returnValue(long messageId, Parcelable response) {
    Intent intent = new Intent(senderUri);
    intent.putExtra("MESSAGE_ID", messageId);
    intent.putExtra("RETURN_VALUE", response);
    context.sendBroadcast(intent);
  }
}
