package com.appcoins.communication.requester;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import com.appcoins.communication.MessageProcessorActivity;

class MessageRequesterSender {
  private final Context context;
  private final String targetPackage;
  private final String targetUri;
  private final String requesterActivityUri;

  MessageRequesterSender(Context context, String targetPackage, String targetUri,
      String requesterActivityUri) {
    this.context = context;
    this.targetPackage = targetPackage;
    this.targetUri = targetUri;
    this.requesterActivityUri = requesterActivityUri;
  }

  public void sendMessage(long requestCode, int methodId, Parcelable arguments) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUri));

    intent.putExtra(MessageProcessorActivity.REQUESTER_ACTIVITY_URI, requesterActivityUri);
    intent.putExtra(MessageProcessorActivity.MESSAGE_ID, requestCode);
    intent.putExtra(MessageProcessorActivity.REQUESTER_PACKAGE_NAME, context.getPackageName());
    intent.putExtra(MessageProcessorActivity.METHOD_ID, methodId);
    intent.putExtra(MessageProcessorActivity.ARGUMENTS, arguments);
    intent.setPackage(targetPackage);

    context.startActivity(intent);
  }
}
