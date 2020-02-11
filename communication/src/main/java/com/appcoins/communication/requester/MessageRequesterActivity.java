package com.appcoins.communication.requester;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;

public abstract class MessageRequesterActivity extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    long requestCode = getIntent().getLongExtra("REQUEST_CODE", -1);
    Parcelable returnValue = getIntent().getParcelableExtra("RETURN_VALUE");
    StaticMessageResponseSynchronizer.getMessageListener()
        .onMessageReceived(requestCode, returnValue);
    finish();
  }
}
