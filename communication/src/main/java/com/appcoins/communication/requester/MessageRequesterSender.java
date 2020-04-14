package com.appcoins.communication.requester;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import com.appcoins.communication.MessageProcessorActivity;

class MessageRequesterSender {
  private final ActivityProvider activityProvider;
  private final String targetPackage;
  private final String targetUri;
  private final String requesterActivityUri;

  MessageRequesterSender(ActivityProvider activityProvider, String targetPackage, String targetUri,
      String requesterActivityUri) {
    this.activityProvider = activityProvider;
    this.targetPackage = targetPackage;
    this.targetUri = targetUri;
    this.requesterActivityUri = requesterActivityUri;
  }

  public void sendMessage(long requestCode, int methodId, Parcelable arguments) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUri));
    Activity activity = activityProvider.getActivity();

    intent.putExtra(MessageProcessorActivity.REQUESTER_ACTIVITY_URI, requesterActivityUri);
    intent.putExtra(MessageProcessorActivity.MESSAGE_ID, requestCode);
    intent.putExtra(MessageProcessorActivity.REQUESTER_PACKAGE_NAME, activity.getPackageName());
    intent.putExtra(MessageProcessorActivity.METHOD_ID, methodId);
    intent.putExtra(MessageProcessorActivity.ARGUMENTS, arguments);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
    intent.setPackage(targetPackage);

    activity.startActivity(intent);
  }
}
