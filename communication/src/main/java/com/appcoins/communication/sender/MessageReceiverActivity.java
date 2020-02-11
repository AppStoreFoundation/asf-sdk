package com.appcoins.communication.sender;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;

public class MessageReceiverActivity extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    long messageId = getIntent().getLongExtra("MESSAGE_ID", -1);
    Parcelable returnValue = getIntent().getParcelableExtra("RETURN_VALUE");
    MessageReceiver.messageListener.onMessageReceived(messageId, returnValue);
    finish();
  }
}
