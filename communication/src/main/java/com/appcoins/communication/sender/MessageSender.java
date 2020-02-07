package com.appcoins.communication.sender;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

class MessageSender {
  private final Context context;
  private final String targetPackage;
  private final String targetUri;

  MessageSender(Context context, String targetPackage, String targetUri) {
    this.context = context;
    this.targetPackage = targetPackage;
    this.targetUri = targetUri;
  }

  public void sendMessage(long messageId, int type, Parcelable arguments) {
    Intent intent = new Intent(targetUri);

    intent.putExtra("MESSAGE_ID", messageId);
    intent.putExtra("TYPE", type);
    intent.putExtra("ARGUMENTS", arguments);
    intent.setPackage(targetPackage);

    context.startActivity(intent);
  }
}
