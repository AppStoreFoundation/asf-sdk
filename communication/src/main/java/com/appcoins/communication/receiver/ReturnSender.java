package com.appcoins.communication.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

public class ReturnSender {
  private final Context context;
  private final String senderUri;

  public ReturnSender(Context context, String senderUri) {
    this.context = context;
    this.senderUri = senderUri;
  }

  public void returnValue(long requestCode, Parcelable response) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(senderUri));
    intent.putExtra("REQUEST_CODE", requestCode);
    intent.putExtra("RETURN_VALUE", response);
    context.sendBroadcast(intent);
  }
}
