package com.appcoins.communication.sender;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

  public void sendMessage(long requestCode, int type, Parcelable arguments) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUri));

    intent.putExtra("MESSAGE_ID", requestCode);
    intent.putExtra("REQUESTER_PACKAGE_NAME", context.getPackageName());
    intent.putExtra("TYPE", type);
    intent.putExtra("ARGUMENTS", arguments);
    intent.setPackage(targetPackage);

    context.startActivity(intent);
  }
}
